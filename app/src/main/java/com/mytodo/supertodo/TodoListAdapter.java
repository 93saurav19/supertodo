package com.mytodo.supertodo;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.mytodo.supertodo.R;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {



    private List<Todo> todoList = new ArrayList<>();
    private OnItemClickListener listener;
    private TodoViewModel tvm;

    public TodoListAdapter(TodoViewModel tvm) {

        this.tvm = tvm;

    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {

        Todo todoItem = todoList.get(position);
        if (todoItem.isCompleted()) {
            holder.layout.setBackgroundResource(R.drawable.gradient_dash);
        }
        else{
            holder.layout.setBackgroundColor(Color.WHITE);
        }
        holder.todo_title.setText(todoItem.getTitle());
        if(todoItem.getPriority() == Priority.HIGH.getValue()){
            holder.todo_title.setTextColor(Color.RED);
        }
        if(todoItem.getPriority() == Priority.MEDIUM.getValue()){
            holder.todo_title.setTextColor(Color.YELLOW);
        }
        if(todoItem.getPriority() == Priority.LOW.getValue()){
            holder.todo_title.setTextColor(Color.GREEN);
        }

        if(todoItem.getCategory() == Category.HOME.getValue()){
            holder.todo_category.setImageResource(R.drawable.home_icon);
        }
        if(todoItem.getCategory() == Category.WORK.getValue()){
            holder.todo_category.setImageResource(R.drawable.office_icon);
        }
        if(todoItem.getCategory() == Category.FAMILY.getValue()){
            holder.todo_category.setImageResource(R.drawable.family_icon);
        }
        if(todoItem.getCategory() == Category.FRIENDS.getValue()){
            holder.todo_category.setImageResource(R.drawable.friends_icon);
        }

        //in some cases, it will prevent unwanted situations
        holder.chkBox.setOnCheckedChangeListener(null);

        holder.todo_date.setText(todoItem.getDuedate());
        holder.chkBox.setChecked(todoItem.isCompleted());

        holder.chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todoItem.setCompleted(isChecked);
                tvm.update(todoItem);
            }
        });

    }

    private void update(Todo todoItem) {

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.d("size",Integer.toString(todoList.size()));
        return todoList.size();
    }

    /**Get todoitem at specified position
     *
     * @param position
     * @return
     */
    public Todo getTodoAt(int position) {
        return todoList.get(position);
    }

    public void clearData() {
    todoList.clear();
    notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private ImageView todo_category;
        LinearLayout layout;
        private MaterialTextView todo_title,todo_description,todo_date;
        CheckBox chkBox;
        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.todo_item_layout);
            todo_title = itemView.findViewById(R.id.todo_item_title);
            todo_description = itemView.findViewById(R.id.todo_item_description);
            todo_category=itemView.findViewById(R.id.todo_item_category);
            todo_date=itemView.findViewById(R.id.todo_item_date);
            chkBox = itemView.findViewById(R.id.todo_item_check);

            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {

                        listener.onItemClick(todoList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Todo todo);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
