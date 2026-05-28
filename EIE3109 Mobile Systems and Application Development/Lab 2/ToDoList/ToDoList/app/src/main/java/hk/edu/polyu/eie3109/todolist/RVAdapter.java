package hk.edu.polyu.eie3109.todolist;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.myViewHolder>  {

    Context context;
    ArrayList<TaskModel> taskModelArrayList;

    public RVAdapter(Context context, ArrayList<TaskModel> taskModelArrayList) {
        this.context = context;
        this.taskModelArrayList = taskModelArrayList;
    }

    @NonNull
    @Override
    public RVAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_row_layout, parent, false);

        return new RVAdapter.myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.myViewHolder holder, int position) {
        holder.cb.setText(taskModelArrayList.get(holder.getAdapterPosition()
        ).getTaskString());
        holder.cb.setChecked(taskModelArrayList.get(holder.getAdapterPosition()
        ).getCompleted());
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.cb.isChecked();
                taskModelArrayList.get(holder.getAdapterPosition()
                ).setCompleted(isChecked);
                if (context instanceof MainActivity) {
                    ((MainActivity) context).saveTasks();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskModelArrayList.size();
    }

    public void deleteItem(int position) {
        taskModelArrayList.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, getItemCount());
    }

    public void editItem(int position) {
        TaskModel task = taskModelArrayList.get(position);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.form_layout);

        EditText etTaskText = dialog.findViewById(R.id.ETTaskText);
        Button btnSubmit = dialog.findViewById(R.id.BNSubmit);

        etTaskText.setText(task.getTaskString());
        btnSubmit.setText("Update");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTask = etTaskText.getText().toString();
                if (!updatedTask.isEmpty()) {
                    task.setTaskString(updatedTask);
                    notifyItemChanged(position);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public Context getContext() {
        return context;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.CBTask);
        }
    }

}
