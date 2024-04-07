package com.example.course_registration_waiting_list_poojaw;

import androidx.appcompat.app.AppCompatActivity; // Import from androidx package
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager; // Import from androidx package
import androidx.recyclerview.widget.RecyclerView; // Import from androidx package
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private StudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SQLite database
        database = new StudentDBHelper(this).getWritableDatabase();

        // Initialize RecyclerView and adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        adapter = new StudentListAdapter(getStudentsFromDatabase());
        adapter = new StudentListAdapter(this, getStudentsFromDatabase());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Add button click listener
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            startActivity(intent);
        });
    }

    private List<Student> getStudentsFromDatabase() {
        List<Student> students = new ArrayList<>();
        Cursor cursor = database.query(StudentContract.StudentEntry.TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_NAME));
            String priority = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_PRIORITY));
            students.add(new Student(id, name, priority));
        }
        cursor.close();
        return students;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setStudents(getStudentsFromDatabase());
        adapter.notifyDataSetChanged();
    }
}
