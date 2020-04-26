package edu.fjnu.actionmode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
    private ListView listView;
    private List<Item> listItem;
    private BaseAdapter adapter;
    private String[] names;
    private int num=0;

    public MainActivity() {
        names = new String[]{"One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.mylist);
        listItem = new ArrayList<Item>();
        for (int i = 0; i < names.length; i++)
        {
            listItem.add(new Item(names[i], false));
        }
        // 创建一个Adapter
        //对listview进行适配器适配
        adapter = new AdapterCur(listItem,MainActivity.this);
        // 为ListView设置Adapter
        listView.setAdapter(adapter);
        //设置listview允许多选模式
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // 调整选定条目
                if (checked == true) {
                    listItem.get(position).setState(true);
                    //实时刷新
                    adapter.notifyDataSetChanged();
                    num++;
                } else {
                    listItem.get(position).setState(false);
                    //实时刷新
                    adapter.notifyDataSetChanged();
                    num--;
                }
                // 用TextView显示
                mode.setTitle("  " + num + " Selected");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        num = 0;
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    case R.id.menu_select:
                        num = names.length;
                        selectAll();
                        adapter.notifyDataSetChanged();
                    default:
                        return false;
                }
            }

            public void selectAll() {
                for(int i = 0; i < names.length; i++){
                    listItem.get(i).setState(true);
                }
            }


            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.activity_action_mode, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                refresh();
                adapter.notifyDataSetChanged();
            }

            //清空
            public void refresh() {
                for(int i = 0; i < names.length; i++){
                    listItem.get(i).setState(false);
                }
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                refresh();
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}
