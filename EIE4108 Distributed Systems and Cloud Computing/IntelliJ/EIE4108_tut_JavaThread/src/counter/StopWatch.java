package counter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StopWatch extends JFrame implements Runnable, ActionListener {
    private TextField tfSecond, tfMinute;
    private Label lbSecond, lbMinute;
    private Button btStart, btStop, btReset, btMyName;
    private Thread clockThread = null;
    private int second, minute;
    private JPanel p1, p2;

    public static void main(String[] args) {
        StopWatch frame = new StopWatch();
        frame.setTitle("StopWatch!");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Constructor for setting up UI and add event listeners
    public StopWatch() {
        setLayout(new GridLayout(2, 1, 0, 0));
        p1 = new JPanel();
        p2 = new JPanel();
        add(p1);
        add(p2);
        lbMinute = new Label("Minute:");
        lbSecond = new Label("Second:");
        tfMinute = new TextField(2);
        tfSecond = new TextField(2);
        tfMinute.setEditable(false);
        tfSecond.setEditable(false);
        btStart = new Button("Start");
        btStop = new Button("Stop");
        btReset = new Button("Reset");
        btMyName = new Button("LI Ming Chun");
        p1.add(lbMinute);
        p1.add(tfMinute);
        p1.add(lbSecond);
        p1.add(tfSecond);
        p1.add(btStart);
        p1.add(btStop);
        p1.add(btReset);
        p2.add(btMyName);
        btStart.addActionListener(this);
        btStop.addActionListener(this);
        btReset.addActionListener(this);
        tfMinute.setText(String.valueOf(minute));
        tfSecond.setText(String.valueOf(second));
    } // End of StopWatch’s constructor

    public void run() {
// Putting the current running thread to sleep for 1 second.
// Wake up the thread immediately if the “Reset” button has
// been pressed.
// If neither the “Reset” button nor the “Stop” button has been
// pressed while the thread is sleeping, increment the
// clock by 1 second when the thread wakes up.
// Put your code here
        while (Thread.currentThread() == clockThread) {
            try {
                Thread.sleep(1000);
                timeTick();

                tfMinute.setText(String.valueOf(minute));
                tfSecond.setText(String.valueOf(second));

            } catch (InterruptedException e) {

            }
        }
    }
    // This method will be called by when any of
// the buttons in the GUI is pressed. So, we take action according to
// which button has been pressed here.

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==btStart) {
// Create and start the clock thread so that it can increment
// the clock every second
// Put your code here
            if (clockThread == null) {
                clockThread = new Thread(this);
                clockThread.start();
            }
        }
        else if (e.getSource()==btStop) {
// Put statement(s) here so that clockThread will not
// increment the clock anymore
            if (clockThread != null) {
                Thread tempThread = clockThread;
                clockThread = null;
                tempThread.interrupt();
            }
        }
        else if (e.getSource()==btReset) {
            reset();
        }
    }

    private void timeTick() {
        second = (second + 1) % 60;
        if (second == 0)
            minute = (minute + 1) % 60;
    }

    private void reset() {
        second = 0;
        minute = 0;
        tfMinute.setText("0");
        tfSecond.setText("0");
// Put statement(s) here so that the sleeping clockThread will
// be woken up immediately. You should make sure that the clock
// will continue to increment for every second. Also make sure
// that the time taken from 0s to 1s really takes one second.
// Hints: Use the interrupt() method of the Thread class.
        if (clockThread != null) {
            clockThread.interrupt();
        }
    }
}