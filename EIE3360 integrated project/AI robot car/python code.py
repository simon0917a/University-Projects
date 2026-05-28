from __future__ import annotations
import os
import sys
import time
import cv2
import numpy as np
from dataclasses import dataclass, field
from typing import List, Optional, Tuple, Dict, Any
from ultralytics import YOLO
# =========================
# 1. 机器人底层库导入
# =========================
try:
    from Library_Robot.lib3360 import motor, servo
except Exception:
    import importlib.util
    import pathlib
    lib_path = pathlib.Path(__file__).resolve().parent / "lib3360.py"
    spec = importlib.util.spec_from_file_location("lib3360", str(lib_path))
    lib = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(lib)
    motor = getattr(lib, "motor")
    servo = getattr(lib, "servo")
# =========================
# 2. 配置区
# =========================
@dataclass
class RobotConfig:
    model_path: str = "./AI_Model/old_version(416).engine"
    camera_index: int = 0
    frame_width: int = 800
    frame_height: int = 600
    model_input_size: int = 416
    crop_scale: float = 1.0
    target_x: int = 400
    line_x1: int = 300
    line_x2: int = 500
    detect_interval: int = 1
    max_correction_delta: int = 800
    debug_visualization: bool = True
    debug_print: bool = True
    target_switch_margin: float = 25.0
    target_match_tolerance: int = 80
    item_classes: List[str] = field(default_factory=lambda: [
        "Coca-Cola",
        "Vita Lemon Tea",
        "Vitasoy Soybean Milk",
        "sprite",
    ])
    ball_classes: List[str] = field(default_factory=lambda: ["green ball", "red ball"])
    # ===== 地面禁抓区参数 =====
    ground_offset_scale: float = 1.7
    forbidden_radius_scale_x: float = 4.10
    ellipse_y_ratio: float = 0.37
    forbidden_expand_size: int = 20
    forbidden_memory_max: int = 15
    # ===== 运动参数 =====
    search_turn_speed: int = 12000
    search_turn_time: float = 0.10
    search_pause_time: float = 0.12
    move_speed: int = 10000        # 正常追踪前进速度
    fast_move_speed: int = 12000   # 启动前进 / 盲抓前进
    reverse_speed: int = 15000     # 后退速度
    turn_speed: int = 10000        # 抓取/投放后的正式转向速度
    straight_bias: int = 950
    center_tolerance: int = 35
    far_threshold: int = 250
    near_threshold: int = 380
    drop_threshold: int = 180
    max_lost_frames: int = 15
    # ===== PID 参数 =====
    kp: float = 9.0
    ki: float = 0.05
    kd: float = 3.5
    integral_limit: float = 1500.0
    pid_max_correction: int = 5000
    # ===== 时序参数 =====
    startup_forward_time: float = 9.0
    backward_time_after_grab: float = 5.5
    backward_time_after_release: float = 4.5
    release_servo_time: float = 0.6
    turn_after_grab_time: float = 2.0
    turn_after_release_time: float = 2.0
    blind_release_forward_time: float = 1.5
# =========================
# 3. 运行时状态
# =========================
@dataclass
class PIDState:
    integral: float = 0.0
    last_error: float = 0.0
    last_time: float = field(default_factory=time.time)
    last_correction: int = 0
    def reset(self) -> None:
        self.integral = 0.0
        self.last_error = 0.0
        self.last_time = time.time()
        self.last_correction = 0

@dataclass
class RuntimeState:
    state: str = "startup_forward"
    state_start_time: float = field(default_factory=time.time)
    grab_start_time: float = 0.0
    release_start_time: float = 0.0
    target_ball: Optional[str] = None
    carrying_item: Optional[str] = None
    lost_frames: int = 0
    last_known_width: int = 0
    last_known_bottom_y: int = 0
    last_forbidden_mask: Optional[np.ndarray] = None
    forbidden_memory_frames: int = 0
    locked_target_cls: Optional[str] = None
    locked_target_center_x: Optional[int] = None
    locked_target_score: Optional[float] = None
    # 新增：搜索脉冲的非阻塞定时器（解决搜索阶段 FPS 只有 2 的核心问题）
    search_turn_end_time: Optional[float] = None
    search_pause_end_time: Optional[float] = None

    def switch(self, new_state: str) -> None:
        self.state = new_state
        self.state_start_time = time.time()
        # 切换状态时重置搜索定时器
        self.search_turn_end_time = None
        self.search_pause_end_time = None
# =========================
# 4. 工具函数
# =========================
def resolve_port() -> Optional[str]:
    if os.getenv("SENDSERIAL_PORT"):
        return os.getenv("SENDSERIAL_PORT")
    if len(sys.argv) > 1:
        return sys.argv[1]
    return None

def get_target_ball(item_name: str) -> str:
    if item_name in ["Coca-Cola", "sprite"]:
        return "green ball"
    return "red ball"

def point_in_mask(mask: np.ndarray, x: int, y: int) -> bool:
    h, w = mask.shape[:2]
    return 0 <= x < w and 0 <= y < h and mask[y, x] > 0
# =========================
# 5. 底盘与舵机控制
# =========================
class RobotActuator:
    def __init__(self, config: RobotConfig, port: Optional[str]) -> None:
        self.cfg = config
        self.port = port
        self.last_motion_cmd = None
        self.last_motor_send_time = 0.0
        self.motor_min_interval = 0.05   # 50ms = 20Hz
        self.servo_state = None

    def stop(self) -> None:
        self._send_motion_limited(0, 0, 0, 0, force=True)

    def forward(self, speed: int = 10000) -> None:
        left_speed = max(0, min(30000, speed + self.cfg.straight_bias))
        right_speed = max(0, min(30000, speed))
        self._send_motion_limited(left_speed, right_speed, 1, 1)

    def backward(self, speed: int = 8000) -> None:
        self._send_motion_limited(speed, speed, 0, 0)

    def turn_left(self, speed: int = 8000) -> None:
        self._send_motion_limited(speed, speed, 0, 1)

    def turn_right(self, speed: int = 8000) -> None:
        self._send_motion_limited(speed, speed, 1, 0)

    def set_servo_home(self) -> None:
        if self.servo_state != "home":
            servo(1400, 1400, mode="once")
            self.servo_state = "home"

    def set_servo_grab_or_release(self) -> None:
        if self.servo_state != "grab_release":
            servo(1000, 1400, mode="once")
            self.servo_state = "grab_release"

    def drive_with_pid(self, correction: int) -> Tuple[int, int]:
        left_speed = max(0, min(30000, self.cfg.move_speed + self.cfg.straight_bias + correction))
        right_speed = max(0, min(30000, self.cfg.move_speed - correction))
        self._send_motion_limited(left_speed, right_speed, 1, 1)
        return left_speed, right_speed

    def _send_motion_limited(
        self,
        left: int,
        right: int,
        dir1: int,
        dir2: int,
        force: bool = False,
    ) -> None:
        now = time.time()
        cmd = (left, right, dir1, dir2)

        # 和上次完全一样，直接不发
        if not force and cmd == self.last_motion_cmd:
            return

        # 和上次差异太小，也先不发
        if not force and self.last_motion_cmd is not None:
            last_left, last_right, last_dir1, last_dir2 = self.last_motion_cmd
            small_change = (
                abs(left - last_left) < 300 and
                abs(right - last_right) < 300 and
                dir1 == last_dir1 and
                dir2 == last_dir2
            )
            if small_change and (now - self.last_motor_send_time) < self.motor_min_interval:
                return

        # 限频：50ms 内最多发一次
        if not force and (now - self.last_motor_send_time) < self.motor_min_interval:
            return

        motor(left, right, dir1=dir1, dir2=dir2, mode="once", port=self.port)
        self.last_motion_cmd = cmd
        self.last_motor_send_time = now



# =========================
# 6. 视觉感知模块
# =========================
class VisionSystem:
    def __init__(self, config: RobotConfig) -> None:
        self.cfg = config
        self.model = YOLO(config.model_path)

    def detect(self, frame: np.ndarray) -> Tuple[List[Dict[str, Any]], np.ndarray, np.ndarray]:
        h, w = frame.shape[:2]
        crop_w = int(w * self.cfg.crop_scale)
        crop_h = int(h * self.cfg.crop_scale)
        crop_x = (w - crop_w) // 2
        crop_y = (h - crop_h) // 2
        crop = frame[crop_y:crop_y + crop_h, crop_x:crop_x + crop_w]
        crop_resized = cv2.resize(crop, (self.cfg.model_input_size, self.cfg.model_input_size))
        results = self.model.predict(
            crop_resized,
            imgsz=self.cfg.model_input_size,
            conf=0.6,
            device=0,
            verbose=False,
        )
        boxes_norm = results[0].boxes.xyxy.cpu().numpy()
        confs = results[0].boxes.conf.cpu().numpy()
        cls_ids = results[0].boxes.cls.cpu().numpy()
        names = self.model.names

        def map_to_original(box: np.ndarray) -> List[int]:
            x1, y1, x2, y2 = box
            return [
                crop_x + int((x1 / self.cfg.model_input_size) * crop_w),
                crop_y + int((y1 / self.cfg.model_input_size) * crop_h),
                crop_x + int((x2 / self.cfg.model_input_size) * crop_w),
                crop_y + int((y2 / self.cfg.model_input_size) * crop_h),
            ]

        detections: List[Dict[str, Any]] = []
        for box_norm, conf, cls_id in zip(boxes_norm, confs, cls_ids):
            x1, y1, x2, y2 = map_to_original(box_norm)
            detections.append({
                "cls_name": names[int(cls_id)],
                "conf": float(conf),
                "x1": x1,
                "y1": y1,
                "x2": x2,
                "y2": y2,
            })
        return detections, crop, crop_resized

    def build_forbidden_mask(
        self,
        frame: np.ndarray,
        detections: List[Dict[str, Any]],
        runtime: RuntimeState,
    ) -> Tuple[np.ndarray, np.ndarray]:
        h, w = frame.shape[:2]
        yellow_mask = np.zeros((h, w), dtype=np.uint8)
        current_forbidden_mask = np.zeros((h, w), dtype=np.uint8)
        ball_detections = [det for det in detections if det["cls_name"] in self.cfg.ball_classes]
        for ball in ball_detections:
            x1, y1, x2, y2 = ball["x1"], ball["y1"], ball["x2"], ball["y2"]
            ball_h = max(1, y2 - y1)
            ball_cx = (x1 + x2) // 2
            ball_bottom_y = y2
            ground_x = ball_cx
            ground_y = ball_bottom_y + int(ball_h * self.cfg.ground_offset_scale)
            ground_x = max(0, min(w - 1, ground_x))
            ground_y = max(0, min(h - 1, ground_y))
            radius_x = max(8, int(ball_h * self.cfg.forbidden_radius_scale_x))
            radius_y = max(5, int(radius_x * self.cfg.ellipse_y_ratio))
            cv2.ellipse(
                current_forbidden_mask,
                (ground_x, ground_y),
                (radius_x, radius_y),
                0, 0, 360,
                255,
                -1
            )
        kernel = np.ones((self.cfg.forbidden_expand_size, self.cfg.forbidden_expand_size), np.uint8)
        current_forbidden_mask = cv2.dilate(current_forbidden_mask, kernel, iterations=1)
        if np.count_nonzero(current_forbidden_mask) > 0:
            forbidden_mask = current_forbidden_mask
            runtime.last_forbidden_mask = current_forbidden_mask.copy()
            runtime.forbidden_memory_frames = self.cfg.forbidden_memory_max
        else:
            if runtime.last_forbidden_mask is not None and runtime.forbidden_memory_frames > 0:
                forbidden_mask = runtime.last_forbidden_mask.copy()
                runtime.forbidden_memory_frames -= 1
            else:
                forbidden_mask = np.zeros((h, w), dtype=np.uint8)
        return yellow_mask, forbidden_mask
# =========================
# 7. 目标选择与 PID
# =========================
def select_target(
    detections: List[Dict[str, Any]],
    target_list: List[str],
    forbidden_mask: np.ndarray,
    runtime: RuntimeState,
    target_x: int,
    switch_margin: float,
    match_tolerance: int,
) -> Optional[Dict[str, Any]]:
    candidates: List[Dict[str, Any]] = []
    for det in detections:
        cls_name = det["cls_name"]
        if cls_name not in target_list:
            continue
        x1, y1, x2, y2 = det["x1"], det["y1"], det["x2"], det["y2"]
        center_x = (x1 + x2) // 2
        object_width = x2 - x1
        error = center_x - target_x
        sample_y = y2
        sample_xs = [
            x1 + (x2 - x1) * 1 // 6,
            x1 + (x2 - x1) * 2 // 6,
            x1 + (x2 - x1) * 3 // 6,
            x1 + (x2 - x1) * 4 // 6,
            x1 + (x2 - x1) * 5 // 6,
        ]
        forbidden_hits = sum(1 for px in sample_xs if point_in_mask(forbidden_mask, px, sample_y))
        in_forbidden = forbidden_hits >= 2
        if runtime.state == "search_item" and in_forbidden:
            continue
        score = abs(error) * 2 - object_width
        candidates.append({
            **det,
            "center_x": center_x,
            "object_width": object_width,
            "error": error,
            "score": score,
        })
    if not candidates:
        runtime.locked_target_cls = None
        runtime.locked_target_center_x = None
        runtime.locked_target_score = None
        return None
    best_target = min(candidates, key=lambda t: t["score"])
    locked_target = None
    if runtime.locked_target_cls is not None and runtime.locked_target_center_x is not None:
        for c in candidates:
            same_class = c["cls_name"] == runtime.locked_target_cls
            close_enough = abs(c["center_x"] - runtime.locked_target_center_x) <= match_tolerance
            if same_class and close_enough:
                locked_target = c
                break
    if locked_target is not None:
        if best_target["score"] < locked_target["score"] - switch_margin:
            selected = best_target
        else:
            selected = locked_target
    else:
        selected = best_target
    runtime.locked_target_cls = selected["cls_name"]
    runtime.locked_target_center_x = selected["center_x"]
    runtime.locked_target_score = selected["score"]
    return selected

def compute_pid(error: float, pid: PIDState, cfg: RobotConfig) -> int:
    now = time.time()
    dt = max(now - pid.last_time, 0.001)
    p_term = cfg.kp * error
    if abs(error) < 150:
        pid.integral += error * dt
        pid.integral = max(-cfg.integral_limit, min(pid.integral, cfg.integral_limit))
    else:
        pid.integral = 0.0
    i_term = cfg.ki * pid.integral
    d_term = cfg.kd * (error - pid.last_error) / dt
    correction = int(p_term + i_term + d_term)
    correction = max(-cfg.pid_max_correction, min(correction, cfg.pid_max_correction))
    max_delta = cfg.max_correction_delta
    lower_bound = pid.last_correction - max_delta
    upper_bound = pid.last_correction + max_delta
    correction = max(lower_bound, min(upper_bound, correction))
    pid.last_error = error
    pid.last_time = now
    pid.last_correction = correction
    return correction
# =========================
# 8. 主控制器
# =========================
class RobotController:
    def __init__(self, config: RobotConfig) -> None:
        self.cfg = config
        self.port = resolve_port()
        self.actuator = RobotActuator(config, self.port)
        self.vision = VisionSystem(config)
        self.runtime = RuntimeState()
        self.pid = PIDState()
        self.frame_count = 0
        self.last_detections: List[Dict[str, Any]] = []
        os.environ.setdefault("DISPLAY", ":0")
        self.cap = cv2.VideoCapture(config.camera_index, cv2.CAP_V4L2)
        self.cap.set(cv2.CAP_PROP_FRAME_WIDTH, config.frame_width)
        self.cap.set(cv2.CAP_PROP_FRAME_HEIGHT, config.frame_height)
        if not self.cap.isOpened():
            raise RuntimeError("Error: Could not open webcam.")
        self.actuator.stop()
        self.actuator.set_servo_home()
        print("=== start ===")

    def debug_log(self, message: str) -> None:
        if self.cfg.debug_print:
            print(message)

    def get_target_list(self) -> List[str]:
        if self.runtime.state == "search_item":
            return self.cfg.item_classes
        if self.runtime.state == "search_drop" and self.runtime.target_ball:
            return [self.runtime.target_ball]
        return []

    def _handle_search_pulse(self) -> None:
        """非阻塞搜索脉冲逻辑（关键修复）：不再使用 time.sleep，避免主循环卡顿导致 FPS 掉到 2"""
        now = time.time()

        # 正在转弯阶段
        if self.runtime.search_turn_end_time is not None:
            if now >= self.runtime.search_turn_end_time:
                self.actuator.stop()
                self.runtime.search_turn_end_time = None
                self.runtime.search_pause_end_time = now + self.cfg.search_pause_time
            else:
                # 持续发送左转指令（安全冗余，电机会保持速度）
                self.actuator.turn_left(self.cfg.search_turn_speed)
            return

        # 正在暂停阶段
        if self.runtime.search_pause_end_time is not None:
            if now >= self.runtime.search_pause_end_time:
                self.runtime.search_pause_end_time = None
            # 暂停期间什么都不做（电机已停止）
            return

        # 准备开始新一次脉冲
        self.actuator.turn_left(self.cfg.search_turn_speed)
        self.runtime.search_turn_end_time = now + self.cfg.search_turn_time

    def handle_target_lost(self) -> None:
        self.runtime.lost_frames += 1
        self.pid.reset()
        if self.runtime.state == "search_item":
            if self.runtime.lost_frames < self.cfg.max_lost_frames:
                self.actuator.stop()
            else:
                # 使用非阻塞脉冲，保持高 FPS
                self._handle_search_pulse()
        # （search_drop 的盲搜逻辑仍保持注释状态）
        if self.runtime.state == "search_drop":
            if self.runtime.lost_frames < self.cfg.max_lost_frames:
                self.actuator.stop()
            else:
                # 使用非阻塞脉冲，保持高 FPS
                self._handle_search_pulse()

    def handle_startup_forward(self, frame: np.ndarray) -> bool:
        if time.time() - self.runtime.state_start_time < self.cfg.startup_forward_time:
            self.actuator.forward(self.cfg.fast_move_speed)
            debug_frame = frame.copy()
            cv2.putText(
                debug_frame,
                "Status: startup_forward",
                (10, 40),
                cv2.FONT_HERSHEY_SIMPLEX,
                1.0,
                (0, 255, 0),
                2,
            )
            cv2.imshow("robot", debug_frame)
            key = cv2.waitKey(20) & 0xFF
            if key in (ord('q'), 27):
                self.safe_shutdown(release_pose=False)
                return False
            return True
        self.actuator.stop()
        self.runtime.switch("search_item")
        self.runtime.lost_frames = 0
        return True

    def handle_target_found(self, target: Dict[str, Any]) -> None:
        cls_name = target["cls_name"]
        error = target["error"]
        object_width = target["object_width"]
        bottom_y = target["y2"]
        self.runtime.last_known_width = object_width
        self.runtime.last_known_bottom_y = bottom_y
        self.runtime.lost_frames = 0
        # 找到目标时立即重置搜索定时器（防止上一轮脉冲残留）
        self.runtime.search_turn_end_time = None
        self.runtime.search_pause_end_time = None
        correction = compute_pid(error, self.pid, self.cfg)
        if self.runtime.state == "search_item":
            if object_width > self.cfg.near_threshold:
                self.runtime.switch("grab")
                self.runtime.grab_start_time = time.time()
                self.runtime.carrying_item = cls_name
                self.runtime.target_ball = get_target_ball(cls_name)
                self.actuator.forward(self.cfg.fast_move_speed)
                self.debug_log(f"[DEBUG] 开始抓取 {cls_name}")
            else:
                self.actuator.set_servo_grab_or_release()
                left_speed, right_speed = self.actuator.drive_with_pid(correction)
                self.debug_log(
                    f"[DEBUG] search_item | selected={cls_name}, error={error}, "
                    f"width={object_width}, L={left_speed}, R={right_speed}"
                )
        elif self.runtime.state == "search_drop":
            if (object_width > self.cfg.drop_threshold):
                self.runtime.switch("release")
                self.runtime.release_start_time = time.time()
                self.debug_log(f"[DEBUG] 准备盲投！宽度={object_width}, bottom_y={bottom_y}")
            else:
                left_speed, right_speed = self.actuator.drive_with_pid(correction)
                self.debug_log(
                    f"[DEBUG] search_drop | error={error}, width={object_width}, "
                    f"bottom_y={bottom_y}, L={left_speed}, R={right_speed}"
                )

    def handle_grab(self) -> None:
        needed_grab_time = 0.4 if self.runtime.carrying_item and "Vita" in self.runtime.carrying_item else 0.7
        if time.time() - self.runtime.grab_start_time < needed_grab_time:
            self.actuator.forward(self.cfg.fast_move_speed)
            return
        self.actuator.stop()
        self.actuator.set_servo_home()
        time.sleep(1)
        self.actuator.backward(self.cfg.reverse_speed)
        time.sleep(self.cfg.backward_time_after_grab)
        self.actuator.stop()
        self.debug_log("[DEBUG] 抓取完成后，正在根据物品类别选择转向...")
        if self.runtime.carrying_item in ["Coca-Cola", "sprite"]:
            self.debug_log(f"[DEBUG] {self.runtime.carrying_item} -> 左转")
            self.actuator.turn_left(self.cfg.turn_speed)
        elif self.runtime.carrying_item in ["Vita Lemon Tea", "Vitasoy Soybean Milk"]:
            self.debug_log(f"[DEBUG] {self.runtime.carrying_item} -> 右转")
            self.actuator.turn_right(self.cfg.turn_speed)
        else:
            self.debug_log(f"[DEBUG] 未知类别 {self.runtime.carrying_item} -> 默认左转")
            self.actuator.turn_left(self.cfg.turn_speed)
        time.sleep(self.cfg.turn_after_grab_time)
        self.actuator.stop()
        self.runtime.switch("search_drop")
        self.debug_log(f"[DEBUG] 抓取完成 → 开始找 {self.runtime.target_ball}")
        self.runtime.last_known_width = 0
        self.runtime.last_known_bottom_y = 0
        self.runtime.lost_frames = 0
        self.pid.reset()
        for _ in range(10):
            self.cap.read()

    def handle_release(self) -> None:
        needed_release_time = self.cfg.blind_release_forward_time
        if time.time() - self.runtime.release_start_time < needed_release_time:
            self.actuator.forward(self.cfg.fast_move_speed)
            return

        self.actuator.stop()

        # 先轻微后退 0.1 秒，再松夹子
        self.debug_log("[DEBUG] 投放前先后退0.1秒...")
        self.actuator.backward(self.cfg.reverse_speed)
        time.sleep(0.2)
        self.actuator.stop()

        # 再松夹子
        self.actuator.set_servo_grab_or_release()
        time.sleep(self.cfg.release_servo_time)

        self.debug_log("[DEBUG] 正在回退...")
        self.actuator.backward(self.cfg.reverse_speed)
        time.sleep(self.cfg.backward_time_after_release)
        self.actuator.stop()

        self.debug_log("[DEBUG] 正在根据物品类别选择转向...")
        if self.runtime.carrying_item in ["Coca-Cola", "sprite"]:
            self.debug_log(f"[DEBUG] {self.runtime.carrying_item} -> 右转")
            self.actuator.turn_right(self.cfg.turn_speed)
        elif self.runtime.carrying_item in ["Vita Lemon Tea", "Vitasoy Soybean Milk"]:
            self.debug_log(f"[DEBUG] {self.runtime.carrying_item} -> 左转")
            self.actuator.turn_left(self.cfg.turn_speed)
        else:
            self.debug_log(f"[DEBUG] 未知类别 {self.runtime.carrying_item} -> 默认左转")
            self.actuator.turn_left(self.cfg.turn_speed)

        time.sleep(self.cfg.turn_after_release_time)
        self.actuator.stop()
        self.actuator.set_servo_home()
        self.debug_log(f"[DEBUG] 掉落完成！{self.runtime.carrying_item} 已放入 {self.runtime.target_ball} 区")
        self.runtime.switch("search_item")
        self.runtime.carrying_item = None
        self.runtime.target_ball = None
        self.runtime.lost_frames = 0
        self.runtime.last_known_width = 0
        self.runtime.last_known_bottom_y = 0
        self.runtime.release_start_time = 0.0
        self.pid.reset()
        for _ in range(10):
            self.cap.read()


    def annotate(
        self,
        frame: np.ndarray,
        yellow_mask: np.ndarray,
        forbidden_mask: np.ndarray,
        target: Optional[Dict[str, Any]],
        fps: float,
    ) -> np.ndarray:
        out = frame.copy()
        font = cv2.FONT_HERSHEY_SIMPLEX
        cv2.line(out, (self.cfg.line_x1, 0), (self.cfg.line_x1, 100), (255, 0, 0), 1)
        cv2.line(out, (self.cfg.line_x2, 0), (self.cfg.line_x2, 100), (255, 0, 0), 1)
        cv2.putText(out, f"FPS: {fps:.2f}", (10, 30), font, 1.0, (0, 255, 0), 2)
        overlay = out.copy()
        overlay[forbidden_mask > 0] = (0, 0, 255)
        out = cv2.addWeighted(out, 0.75, overlay, 0.25, 0)
        current_target_name = "None"
        current_object_width = "N/A"
        current_error = 0
        if target is not None:
            x1, y1, x2, y2 = target["x1"], target["y1"], target["x2"], target["y2"]
            center_x = target["center_x"]
            conf = target["conf"]
            current_target_name = target["cls_name"]
            current_object_width = str(target["object_width"])
            current_error = target["error"]
            cv2.rectangle(out, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.rectangle(
                out,
                (center_x - 5, (y1 + y2) // 2 - 5),
                (center_x + 5, (y1 + y2) // 2 + 5),
                (0, 255, 255),
                2,
            )
            label = f"{target['cls_name']} {conf:.2f}"
            cv2.putText(out, label, (x1, max(20, y1 - 8)), font, 0.5, (255, 255, 255), 1, cv2.LINE_AA)
        cv2.putText(out, f"Status: {self.runtime.state}", (10, 60), font, 0.6, (0, 255, 0), 2)
        cv2.putText(out, f"Target: {current_target_name} | Width: {current_object_width}", (10, 90), font, 0.6, (0, 255, 0), 2)
        cv2.putText(out, f"Error: {current_error}", (10, 120), font, 0.6, (0, 255, 0), 2)
        if self.runtime.carrying_item:
            cv2.putText(out, f"Carrying: {self.runtime.carrying_item}", (10, 150), font, 0.6, (0, 255, 0), 2)
        return out

    def safe_shutdown(self, release_pose: bool = True) -> None:
        print("检测到退出，正在安全关闭...")
        self.actuator.stop()
        if release_pose:
            self.actuator.set_servo_grab_or_release()
        else:
            self.actuator.set_servo_home()
        self.cap.release()
        cv2.destroyAllWindows()
        print("=== 程序结束 ===")

    def run(self) -> None:
        try:
            while True:
                ret, frame = self.cap.read()
                if not ret:
                    break

                if self.runtime.state == "startup_forward":
                    if not self.handle_startup_forward(frame):
                        return
                    if self.runtime.state == "startup_forward":
                        continue

                if self.runtime.state == "grab":
                    self.handle_grab()
                    annotated = frame.copy()
                    cv2.putText(
                        annotated,
                        "Status: grab",
                        (10, 60),
                        cv2.FONT_HERSHEY_SIMPLEX,
                        0.6,
                        (0, 255, 0),
                        2,
                    )
                    if self.cfg.debug_visualization and int(time.time() * 10) % 3 == 0:
                        cv2.imshow("robot", annotated)
                    key = cv2.waitKey(1) & 0xFF
                    if key in (ord('q'), 27):
                        self.safe_shutdown(release_pose=True)
                        return
                    continue

                start_time = time.time()
                self.frame_count += 1
                if self.frame_count % self.cfg.detect_interval == 0:
                    detections, _, _ = self.vision.detect(frame)
                    self.last_detections = detections
                else:
                    detections = self.last_detections

                yellow_mask, forbidden_mask = self.vision.build_forbidden_mask(frame, detections, self.runtime)
                target_list = self.get_target_list()
                target = select_target(
                    detections=detections,
                    target_list=target_list,
                    forbidden_mask=forbidden_mask,
                    runtime=self.runtime,
                    target_x=self.cfg.target_x,
                    switch_margin=self.cfg.target_switch_margin,
                    match_tolerance=self.cfg.target_match_tolerance,
                )

                if target is not None:
                    self.handle_target_found(target)
                else:
                    self.handle_target_lost()

                if self.runtime.state == "release":
                    self.handle_release()
                    annotated = frame.copy()
                    cv2.putText(
                        annotated,
                        "Status: release",
                        (10, 60),
                        cv2.FONT_HERSHEY_SIMPLEX,
                        0.6,
                        (0, 255, 0),
                        2,
                    )
                    if self.cfg.debug_visualization and int(time.time() * 10) % 3 == 0:
                        cv2.imshow("robot", annotated)
                    key = cv2.waitKey(1) & 0xFF
                    if key in (ord('q'), 27):
                        self.safe_shutdown(release_pose=True)
                        return
                    continue

                fps = 1.0 / max(time.time() - start_time, 1e-6)
                if self.frame_count % 20 == 0:
                    print(f"[FPS] {fps:.2f}")

                if self.cfg.debug_visualization:
                    annotated = self.annotate(frame, yellow_mask, forbidden_mask, target, fps)
                else:
                    annotated = frame

                if self.cfg.debug_visualization and int(time.time() * 10) % 3 == 0:
                    cv2.imshow("robot", annotated)

                key = cv2.waitKey(1) & 0xFF
                if key in (ord('q'), 27):
                    self.safe_shutdown(release_pose=True)
                    return
        finally:
            self.cap.release()
            cv2.destroyAllWindows()
            print("=== 程序结束 ===")
# =========================
# 9. 程序入口
# =========================
def main() -> None:
    config = RobotConfig()
    controller = RobotController(config)
    controller.run()

if __name__ == "__main__":
    main()