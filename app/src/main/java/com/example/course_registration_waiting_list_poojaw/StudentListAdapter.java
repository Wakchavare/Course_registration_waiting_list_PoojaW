package com.example.course_registration_waiting_list_poojaw;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private List<Student> students;
    private Context context;

    public StudentListAdapter(Context context, List<Student> students) {
        this.students = students;
        this.context = context;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View studentView = inflater.inflate(R.layout.item_student, parent, false);
        return new ViewHolder(studentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student student = students.get(position);

        holder.idTextView.setText(String.valueOf(student.getId()));
        holder.nameTextView.setText(student.getName());
        holder.priorityTextView.setText(student.getPriority());

        holder.deleteButton.setOnClickListener(v -> {
            // Delete the student from the database
            deleteStudentFromDatabase(student.getId());

            // Remove the student from the list
            students.remove(position);

            // Notify the adapter that data has changed
            notifyDataSetChanged();
        });

        holder.updateButton.setOnClickListener(v -> {
            // Handle update button click
            Intent intent = new Intent(context, AddStudentActivity.class);
            intent.putExtra("student", student); // Pass the student object
            context.startActivity(intent);
        });
    }

    private void deleteStudentFromDatabase(int studentId) {
        StudentDBHelper dbHelper = new StudentDBHelper(context);
        dbHelper.deleteStudent(studentId);
    }


    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView idTextView;
        public TextView nameTextView;
        public TextView priorityTextView;
        public Button deleteButton;
        public Button updateButton;

        public ViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.idTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            updateButton = itemView.findViewById(R.id.updateButton);
        }
    }
}
