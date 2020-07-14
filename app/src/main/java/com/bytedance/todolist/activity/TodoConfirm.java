package com.bytedance.todolist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;

import java.util.Date;

public class TodoConfirm extends Activity {
    private EditText editText;
    public static final String KEY = "result_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_additem);
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.Text1);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(KEY, input);
                if (input.isEmpty()) {
                    setResult(RESULT_CANCELED, intent);
                } else {
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }
}
