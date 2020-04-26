package edu.fjnu.xmlmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 定义“字体大小”菜单项的标识
    private static final int FONT_10 = 0x111;
    private static final int FONT_16 = 0x114;
    private static final int FONT_20 = 0x115;
    // 定义“普通菜单项”的标识
    private static final int PLAIN_ITEM = 0x116;
    // 定义“字体颜色”菜单项的标识
    private static final int FONT_RED = 0x117;
    private static final int FONT_BLACK = 0x118;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取侧式文本
        text = findViewById(R.id.txt);
    }
    // 当用户单击MENU键时触发该方法
    @Override public boolean onCreateOptionsMenu(Menu menu)
    {
        // -------------向menu中添加“字体大小”的子菜单-------------
        SubMenu fontMenu = menu.addSubMenu("字体大小");
        // 设置菜单头的标题
        fontMenu.setHeaderTitle("选择字体大小");
        fontMenu.add(0, FONT_10, 0, "10号字体");
        fontMenu.add(0, FONT_16, 0, "16号字体");
        fontMenu.add(0, FONT_20, 0, "20号字体");
        // 设置菜单头的标题
        menu.add(0, PLAIN_ITEM, 0, "普通菜单项");
        // 设置菜单头的标题
        SubMenu colorMenu = menu.addSubMenu("字体颜色");
        colorMenu.setHeaderTitle("选择文字颜色");
        colorMenu.add(0, FONT_RED, 0, "红色");
        colorMenu.add(0, FONT_BLACK, 0, "黑色");
        return super.onCreateOptionsMenu(menu);
    }
    // 选项菜单的菜单项被单击后的回调方法
    @Override public boolean onOptionsItemSelected(MenuItem mi)
    {
        // 判断单击的是哪个菜单项，并有针对性地做出响应
        switch (mi.getItemId())
        {
            case FONT_10: text.setTextSize(10 * 2);	break;
            case FONT_16: text.setTextSize(16 * 2); break;
            case FONT_20: text.setTextSize(20 * 2); break;
            case FONT_RED: text.setTextColor(Color.RED); break;
            case FONT_BLACK: text.setTextColor(Color.BLACK); break;
            case PLAIN_ITEM:
                Toast toast=Toast.makeText(MainActivity.this,"您单击了普通菜单项",Toast.LENGTH_SHORT   );
                //设置toast位置
                toast.setGravity(Gravity.CENTER, 0, 200);
                toast.show();
                break;
        }
        return true;
    }
}
