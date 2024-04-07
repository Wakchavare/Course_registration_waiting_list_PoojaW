package com.example.course_registration_waiting_list_poojaw;

import androidx.appcompat.app.AppCompatActivity; // Import from androidx package
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddStudentActivity extends AppCompatActivity {

    private EditText nameEditText;
    private Spinner prioritySpinner;
    private ArrayAdapter<CharSequence> adapter;
    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        Button saveButton = findViewById(R.id.saveButton);

        // Create an array adapter with priority options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        // Check if the activity is in "update" mode
        if (getIntent().hasExtra("student")) {
            Log.d("Priority Debug","Inside Update Student");
            setTitle("Update Student"); // Set the title to indicate update mode

            // Retrieve existing student details from intent extras
            Student student = (Student) getIntent().getSerializableExtra("student");

            // Pre-populate fields with existing student details
            nameEditText.setText(student.getName());

            // Set spinner selection based on existing priority
            String[] priorities = getResources().getStringArray(R.array.priority_options);
            String existingPriority = student.getPriority();
            Log.d("Priority Debug", "Existing: "+existingPriority); // Debug log to check existing prioritycom
            for (int i = 0; i < priorities.length; i++) {
                Log.d("Priority Debug","Current: "+priorities[i] + " Existing: "+existingPriority);
                if (existingPriority.equals(priorities[i])) {
                    prioritySpinner.setSelection(i);
                    Log.d("Priority Debug", "Match found at index: " + i + " " + priorities[i] ); // Debug log for match found
                    break;
                }
            }
        } else {
            setTitle("Add Student"); // Set the title to indicate add mode
            Log.d("Priority Debug","Inside Add Student");
        }



        // Save button click listener
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String selectedPriority = prioritySpinner.getSelectedItem().toString();

            if (getIntent().hasExtra("student")) {
                // If in "update" mode, update the existing student
                Student student = (Student) getIntent().getSerializableExtra("student");
                updateStudentInDatabase(student.getId(), name, selectedPriority);
            } else {
                // If in "add" mode, add a new student
                saveStudentToDatabase(name, selectedPriority);
            }
            finish(); // Go back to MainActivity after saving or updating
        });
    }


    private void updateStudentInDatabase(int id, String name, String selectedPriority) {
        // Get a writable database instance
        StudentDBHelper dbHelper = new StudentDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object to hold the new values
        ContentValues values = new ContentValues();
        values.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        values.put(StudentContract.StudentEntry.COLUMN_PRIORITY, selectedPriority);

        // Define the WHERE clause to specify which student to update
        String selection = StudentContract.StudentEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        // Perform the update operation
        int rowsUpdated = db.update(
                StudentContract.StudentEntry.TABLE_NAME,   // Table to update
                values,                                    // New values to apply
                selection,                                 // WHERE clause
                selectionArgs                              // Arguments for the WHERE clause
        );

        // Close the database connection
        db.close();

        // Check if the update was successful
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show();
        }
    }


//    private void saveStudentToDatabase(String name, String priority) {
//        ContentValues values = new ContentValues();
//        values.put(StudentContract.StudentEntry.COLUMN_NAME, name);
//        values.put(StudentContract.StudentEntry.COLUMN_PRIORITY, priority);
//        database.insert(StudentContract.StudentEntry.TABLE_NAME, null, values);
//    }

    private void saveStudentToDatabase(String name, String priority) {
        // Initialize StudentDBHelper and get a writable database
        StudentDBHelper dbHelper = new StudentDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        values.put(StudentContract.StudentEntry.COLUMN_PRIORITY, priority);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(StudentContract.StudentEntry.TABLE_NAME, null, values);

        // Check if insert was successful and show a Toast message
        if (newRowId == -1) {
            // If the row ID is -1, there was an error with insertion.
            Toast.makeText(this, "Error with saving student.", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Student saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }

        // Close the database connection
        db.close();
    }

}

