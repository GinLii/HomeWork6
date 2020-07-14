package com.bytedance.todolist.activity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder {
    private Long ID;
    private CheckBox mContent;
    private TextView mTimestamp;
    private ImageView mImg;
    TodoListAdapter.OnRecordClickListener mListener;

    public TodoListItemHolder(@NonNull View itemView, final TodoListAdapter.OnRecordClickListener Listener) {
        super(itemView);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        mImg = itemView.findViewById(R.id.tv_del);
        mListener = Listener;
    }

    @SuppressLint("ResourceAsColor")
    public void bind(TodoListEntity entity) {
        mContent.setText(entity.getContent());
        mTimestamp.setText(formatDate(entity.getTime()));
        ID = entity.getId();

        if(entity.getFinish()) {
            mContent.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); ;
            mContent.setChecked(true);
            mContent.setOnCheckedChangeListener(null);
        }

        mContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mListener != null) {
                    mListener.onCheck(buttonView, ID, isChecked);
                }
            }
        });

        mImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (mListener != null) {
                    mListener.onButtonClick(view, ID);
                }
            }
        });
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}