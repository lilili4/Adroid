package edu.fjnu.intendtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //清空输入框内容内容
    public void clear(View source){
        EditText editText = findViewById(R.id.target_url);
        editText.setText("");
    }

    public void searching(View source){
        // Create the text message with a string
        Intent mewebIntent = new Intent();
        mewebIntent.setAction(Intent.ACTION_VIEW);
        //获取输入的网址
        EditText editTarget = findViewById(R.id.target_url);
        String target = editTarget.getText().toString();
        //把网址信息放到data中
        mewebIntent.setData(Uri.parse(target));
        // 创建Intent显示选择器对话框
        Intent chooser = Intent.createChooser(mewebIntent,"查看"+target);
        // 验证Intent至少有一项活动
        if (mewebIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
