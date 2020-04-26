package edu.fjnu.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String[] names;
    private int[] imageIds;

    public MainActivity() {
        names = new String[]{"Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant"};
        imageIds = new int[]{
                R.drawable.lion,
                R.drawable.tiger,
                R.drawable.monkey,
                R.drawable.dog,
                R.drawable.cat,
                R.drawable.elephant};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 创建一个List集合，List集合的元素是Map
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < names.length; i++)
        {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("personName", names[i]);
            listItem.put("header", imageIds[i]);
            listItems.add(listItem);
        }
        // 创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.simple_item, new String[]{"personName", "header"},
                new int[]{R.id.name, R.id.header});
        ListView list = findViewById(R.id.mylist);
        // 为ListView设置Adapter
        list.setAdapter(simpleAdapter);
    // 为ListView的列表项的单击事件绑定事件监听器
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Toast toast=Toast.makeText(MainActivity.this,names[position],Toast.LENGTH_SHORT   );
//            //设置toast位置
//            toast.setGravity(Gravity.CENTER, 0, 200);
//            toast.show();
            Toast toast = new Toast(MainActivity.this);
            //设置toast位置
            toast.setGravity(Gravity.CENTER, 0, 200);
            //创建一个Imageview
            ImageView image = new ImageView(MainActivity.this);
            image.setImageResource(imageIds[position]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);//两个400分别为添加图片的大小
            image.setLayoutParams(params);

            //创建一个linearLayout容器
            LinearLayout linearlayout = new LinearLayout(MainActivity.this);
            //创建一个TextView
            TextView text = new TextView(MainActivity.this);
            text.setText(names[position]+"  ");
            text.setTextSize(25);
            text.setHeight(100);
            text.setGravity(Gravity.CENTER);
            linearlayout.addView(text);
            linearlayout.addView(image);
            toast.setView(linearlayout);
            toast.show();
        }
    });
    // 为ListView的列表项的选中事件绑定事件监听器
    list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            Toast toast=Toast.makeText(MainActivity.this,names[position]+"被选中",Toast.LENGTH_SHORT   );
            //设置toast位置
            toast.setGravity(Gravity.CENTER, 0, 200);
            toast.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {
        }
    });
    }
}
