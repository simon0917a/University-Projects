package hk.edu.polyu.eie3109.todolist;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView RVTaskList;
    ArrayList<TaskModel> taskModelArrayList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    RVAdapter RVAdapter;
    private static final String SHARED_PREFS_NAME = "todo_list_prefs";
    private static final String TASKS_KEY = "tasks_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RVTaskList = findViewById(R.id.RVTaskList);
        RVTaskList.setLayoutManager(new LinearLayoutManager(this));

        loadTasks();
        if (taskModelArrayList.isEmpty()) {
            setUpTaskModels();
            saveTasks();
        }

        RVAdapter = new RVAdapter(this, taskModelArrayList);
        RVTaskList.setAdapter(RVAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(RVAdapter));
        itemTouchHelper.attachToRecyclerView(RVTaskList);

        floatingActionButton = findViewById(R.id.FABAddTask);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog addForm = new Dialog(MainActivity.this);
                addForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
                addForm.setContentView(R.layout.form_layout);
                Button BNSave = addForm.findViewById(R.id.BNSubmit);
                BNSave.setText("Add");
                BNSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText ETNewTask = addForm.findViewById(R.id.ETTaskText);
                        taskModelArrayList.add(new TaskModel(ETNewTask.getText().toString(), false));
                        RVAdapter.notifyDataSetChanged();
                        saveTasks();
                        addForm.dismiss();
                    }
                });
                addForm.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTasks();
    }

    private void setUpTaskModels() {
        String[] taskStrings = new String[20];
        for (int i = 0; i < 20; i++) {
            taskStrings[i] = "Task " + i;
        }
        for (int i = 0; i < taskStrings.length; i++) {
            taskModelArrayList.add(new TaskModel(taskStrings[i], (i % 2 == 0)));
        }
    }

    public void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();
        for (TaskModel task : taskModelArrayList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("task", task.getTaskString());
                jsonObject.put("completed", task.getCompleted());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(TASKS_KEY, jsonArray.toString());
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(TASKS_KEY, null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                taskModelArrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String taskString = jsonObject.getString("task");
                    boolean completed = jsonObject.getBoolean("completed");
                    taskModelArrayList.add(new TaskModel(taskString, completed));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
