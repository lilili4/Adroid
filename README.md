- [实验工程代码](https://github.com/lilili4/Android/tree/master/实验3/Ui)

## simpleAdapter实现：

先定义一个ListView,他就相当于总体的空骨架

```xml
<!-- 定义一个ListView -->
<ListView android:id="@+id/mylist"    
  android:layout_width="match_parent"    
  android:layout_height="wrap_content" />
```

然后定义每一行里的结构，相当于又规定了每层的骨架是什么样子

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:paddingLeft="10dp"
    android:paddingRight="10dp">
    <!-- 定义一个TextView，用于作为列表项的一部分。 -->
    <TextView
        android:id="@+id/name"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:gravity="center_vertical"
        android:textSize="25sp"
        />
    <!-- 定义一个ImageView，用于作为列表项的一部分。 -->
    <ImageView
        android:id="@+id/header"
        android:layout_width="60dp"
        android:layout_height="60dp"/>
</LinearLayout>
```

然后开始用java代码往骨架里填充内容：

上面的骨架里每层有两个组件，所以就定义个两个列表（文字和图片），

```java
// 创建一个List集合，List集合的元素是Map
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < names.length; i++)
        {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("personName", names[i]);
            listItem.put("header", imageIds[i]);
            listItems.add(listItem);
        }
```

接着就是用适配器吧两个列表装起来，装的时候要注意顺序，要和每层骨架中内容的类型相对，再然后就是通过这个适配器把内容转载到骨架里，骨架终于有了肉身：

```java
SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.simple_item, new String[]{"personName", "header"},
                new int[]{R.id.name, R.id.header});
        ListView list = findViewById(R.id.mylist);
        // 为ListView设置Adapter
        list.setAdapter(simpleAdapter);
```

#### 效果：

![](D:/Git/Adroid_repository/images/实验3/1.png)

然后接着要为这死的身躯注入灵魂，给他设置监听器，这样它就活起来了，可以向应用户的操作：

设置了两个监听器：选中和点击，根据操作类型给用户发个toast就ok了

```java
// 为ListView的列表项的单击事件绑定事件监听器
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
```

选中时：

![](D:/Git/Adroid_repository/images/实验3/2.png)

点击时：

![](D:/Git/Adroid_repository/images/实验3/3.png)



## AlertDialog实现：

这个我就实现的比较简单了，直接就是定义个TableLayout然后把这个table装到用Builder构建出来的alertDialog中，完。

这个就是TableLayout，这里主要是新学到了可以直接在xml中加载图片、设置事件响应函数。

```xml
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
	android:id="@+id/loginForm"
	android:layout_width="match_parent"
	android:layout_height="match_parent">

	<!-- 标题栏 -->
	<TableRow
		android:paddingStart="12dp"
		android:background="@color/orringe"
		android:paddingVertical="15dp">
		<ImageView android:id="@+id/titleimage"
			android:layout_width="match_parent"
			android:layout_gravity="center"
			android:src="@drawable/androidapp"
			/>
	</TableRow>

	<TableRow android:paddingStart="12dp">
		<!-- 输入用户名的文本框 -->
		<EditText android:id="@+id/editText1"
			android:layout_width="match_parent"
			android:layout_height="wrap_content"
			android:hint="Username"
			android:textSize="25sp"
			android:maxLength="16"
			android:ems="12"
			android:selectAllOnFocus="true" />
	</TableRow>

	<TableRow android:paddingStart="12dp">
		<!-- 输入密码的文本框 -->
		<EditText android:id="@+id/editText2"
			android:layout_width="match_parent"
			android:layout_height="wrap_content"
			android:hint="Password"
			android:textSize="25sp"
			android:maxLength="16"
			android:inputType="textPassword" />
	</TableRow>

	<!-- 水平的按钮 -->
	<LinearLayout
		android:layout_width="match_parent"
		android:layout_height="wrap_content">
		<Button
			android:layout_height="match_parent"
			android:layout_width="wrap_content"
			android:layout_weight="1"
			android:text="Cancel"
			android:textSize="22sp"
			android:onClick="cancel"/>
		<Button
			android:layout_height="match_parent"
			android:layout_width="wrap_content"
			android:layout_weight="1"
			android:text="Sign in"
			android:textSize="22sp"
			android:onClick="sign_in"/>
	</LinearLayout>
</TableLayout>
```

表格张这个样

![](D:/Git/Adroid_repository/images/实验3/4.png)

几个事件响应函数：

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cancel(View source){
        Toast toast=Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_SHORT   );
        //设置toast位置
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
    }

    public void sign_in(View source){
        Toast toast=Toast.makeText(MainActivity.this,"Signin",Toast.LENGTH_SHORT   );
        //设置toast位置
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
    }

    public void alertDialog(View source)
    {
        // 加载\res\layout\login.xml界面布局文件
        TableLayout loginForm = (TableLayout) getLayoutInflater().inflate(R.layout.login, null);
        new AlertDialog.Builder(this)
                // 设置对话框显示的View对象
                .setView(loginForm)
                // 创建并显示对话框
                .create().show();
    }
}
```

合体后：

![](D:/Git/Adroid_repository/images/实验3/5.png)

![](D:/Git/Adroid_repository/images/实验3/6.png)

找了很久不知道他那两个积极和消极按钮按怎么给他们设置样式。



## XML定义菜单

一看到菜单栏我就想到啥啥啥的回调方法。optionmenu的实现只要是就靠重写onOptionsItemSelected就可以实现，然后再配上onCreateOptionsMenu就可以实现点击的功能响应

重写回调反法：onOptionsItemSelected(MenuItem mi)：

```java
public boolean onOptionsItemSelected(MenuItem mi)
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
```

onCreateOptionsMenu:

```java
public boolean onCreateOptionsMenu(Menu menu)
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

```

效果：

![](D:/Git/Adroid_repository/images/实验3/7.png)

设置字体大小：

![](D:/Git/Adroid_repository/images/实验3/8.png)

设置字体：

![](D:/Git/Adroid_repository/images/实验3/9.png)

toast'提示：

![](D:/Git/Adroid_repository/images/实验3/10.png)



## ActionMode

actionmode的实现主要是在实现 AbsListView.MultiChoiceModeListener 接口，用setMultiChoiceModeListener()为组件设置该接口，所以代码上看上去几个主要的函数都是作为setMultiChoiceModeListener匿名类的成员函数传给AbsListView.MultiChoiceModeListener。一共四个重要的函数：onItemCheckedStateChanged（响应列表每行点击或者状态改变）、onActionItemClicked（响应顶部菜单栏的点击事件）、onCreateActionMode（创建时初始化，只执行一次）、onPrepareActionMode（每次使用时调用）、onDestroyActionMode（清理）

具体代码：

```java
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

```

列表就是和第一个的实现完全一样的了，实现效果如下：

![](D:/Git/Adroid_repository/images/实验3/11.png)

点击长按列表项：

![](D:/Git/Adroid_repository/images/实验3/12.png)













