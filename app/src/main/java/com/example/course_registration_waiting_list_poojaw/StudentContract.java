package com.example.course_registration_waiting_list_poojaw;

import android.provider.BaseColumns;

public final class StudentContract {
    private StudentContract() {
    }

    public static class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "students";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRIORITY = "priority";
    }
}
