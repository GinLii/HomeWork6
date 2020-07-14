package com.bytedance.todolist.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.bytedance.todolist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private TodoListAdapter mAdapter;
    private FloatingActionButton mFab;
    private static final int REQUEST_CODE_1 = 1357;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TodoListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);


        mAdapter.setOnRecordClickListener(new TodoListAdapter.OnRecordClickListener() {
            @Override
            public void onButtonClick(View view, final long ID){
                new Thread() {
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                        dao.deleteOne(ID+"");
                        loadFromDatabase();
                    }
                }.start();
            }

            @Override
            public void onCheck(View view, final long ID, boolean isChecked) {
                if (isChecked) {
                    new Thread() {
                        @Override
                        public void run() {
                            TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                            dao.done(ID+"",1+"");
                            loadFromDatabase();
                        }
                    }.start();

                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                            dao.done(ID+"",0+"");
                            loadFromDatabase();
                        }
                    }.start();
                }
            }
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Intent intent = new Intent(TodoListActivity.this, TodoConfirm.class);
                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                        dao.deleteAll();
                        for (int i = 0; i < 20; ++i) {
                            dao.addTodo(new TodoListEntity("This is " + i + " item", new Date(System.currentTimeMillis())));
                        }
                        Snackbar.make(mFab, R.string.hint_insert_complete, Snackbar.LENGTH_SHORT).show();
                        loadFromDatabase();
                    }
                }.start();
                return true;
            }
        });
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        new Thread() {
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                final List<TodoListEntity> entityList = dao.loadAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(entityList);
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_1) {
            if (resultCode == RESULT_OK && data != null) {
                new Thread() {
                    @Override
                    public void run() {
                        String result = data.getStringExtra(TodoConfirm.KEY);
                        TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                        dao.addTodo(new TodoListEntity(result, new Date(System.currentTimeMillis())));
                        loadFromDatabase();
                    }
                }.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "没有获取信息", Toast.LENGTH_LONG).show();
            }
        }
    }
}
