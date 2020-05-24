## NotesPad功能扩充：期中部分

要在开源的NotesPad上进行功能扩充，增加时间戳和搜索功能，第一步必须要先大概了解源代码的结构、使用了哪几种组件。大概及以下笔记：

该项目的默认启动activity是“NotesList”，所以就先从这个类开始记。

列表的实现：使用SimpleCursorAdapter

使用到optionmenu（实现onCreateOptionsMenu()、onPrepareOptionsMenu()、onOptionsItemSelected()这三个抽象函数）和contextmenu（实现onCreateContextMenu()、onContextItemSelected()、onListItemClick()这三个抽象函数）两种菜单，用于响应该页面的所有用户操作。

页面跳转方式：使用隐式intent

数据库的管理：使用ContentProvider管理工具，但资源并没有对其他应用开放

数据库里的内容： 数据库名：note_pad.db ，包含一个表notes（_ID，title, test, created, modified），并且重写了增删改查等几个抽象函数

页面样式使用android:theme的方式设置，采用系统提供的Theme.Holo.Light风格（白色主题）

项目中需要用到的固定常量都定义到NotePad单例中

在记事本的编辑页面中使用optionmenu和textview编辑框，涉及到保存，更新，删除操作

知道以上差不多就够完成其中部分的更改了。



### 添加时间戳：

因为创建时间而最近修改时间在项目原本的数据表notes中已经有对应的属性，所以要添加时间戳只需在保存或者修改时多增加更新一下时间属性就可以。

```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle all of the possible menu actions.
    switch (item.getItemId()) {
    case R.id.menu_save:
        String text = mText.getText().toString();
        updateNote(text, null);
        finish();
        break;
    case R.id.menu_delete:
        deleteNote();
        finish();
        break;
    case R.id.menu_revert:
        cancelNote();
        break;
    }
    return super.onOptionsItemSelected(item);
}

```

上面这个函数是noteEditor中实现optionmenu菜单响应的函数，可以看出保存操作调用updateNote这个函数，所以就到updateNote函数中

```java

private final void updateNote(String text, String title) {

    // Sets up a map to contain values to be updated in the provider.
    ContentValues values = new ContentValues();
    values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, getFormatData()); //TODO 修改

    // If the action is to insert a new note, this creates an initial title for it.
    if (mState == STATE_INSERT) {

        // If no title was provided as an argument, create one from the note text.
        if (title == null) {

            // Get the note's length
            int length = text.length();

            // Sets the title by getting a substring of the text that is 31 characters long
            // or the number of characters in the note plus one, whichever is smaller.
            //取前30个字符做title
            title = text.substring(0, Math.min(30, length));

            Log.d(TAG, "1title-->"+title);
            // If the resulting length is more than 30 characters, chops off any
            // trailing spaces
            //若前30个字符中有空格则把最后一个空格之前的内容当作title
            if (length > 30) {
                int lastSpace = title.lastIndexOf(' ');
                if (lastSpace > 0) {
                    title = title.substring(0, lastSpace);
                }
            }
            // In the values map, sets the value of the title
            values.put(NotePad.Notes.COLUMN_NAME_TITLE, title);
        }
        Log.d(TAG, "2text-->"+text);
        Log.d(TAG, "2title-->"+title);
    } else if (title != null) {
        // In the values map, sets the value of the title
        values.put(NotePad.Notes.COLUMN_NAME_TITLE, title);
    }

    // This puts the desired notes text into the map.
    values.put(NotePad.Notes.COLUMN_NAME_NOTE, text);

    /*
     * Updates the provider with the new values in the map. The ListView is updated
     * automatically. The provider sets this up by setting the notification URI for
     * query Cursor objects to the incoming URI. The content resolver is thus
     * automatically notified when the Cursor for the URI changes, and the UI is
     * updated.
     * Note: This is being done on the UI thread. It will block the thread until the
     * update completes. In a sample app, going against a simple provider based on a
     * local database, the block will be momentary, but in a real app you should use
     * android.content.AsyncQueryHandler or android.os.AsyncTask.
     */
    getContentResolver().update(
            mUri,    // The URI for the record to update.
            values,  // The map of column names and new values to apply to them.
            null,    // No selection criteria are used, so no where columns are necessary.
            null     // No where columns are used, so no where arguments are necessary.
        );
}
```

在提交更新之前往ContentValues中再增加当前的时间：

`values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, getFormatData());` 

getFormatData函数为自定义添加的一个以String放回格式化时间的函数：

```java
//新定义函数获取格式化的当前系统时间
@TargetApi(Build.VERSION_CODES.N)
public String getFormatData(){
    long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date mdata=new Date(time);
    String mtime=format.format(mdata);
    return mtime;
}
```

然后表里就有最新的更新时间戳了，下一步只要在notes列表读取是多读取时间戳属性，多创建一个textview显示时间。

先修改noteslist_item.xml添加textview

```xml

<?xml version="1.0" encoding="utf-8"?>
<!-- Copyright (C) 2010 The Android Open Source Project

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
  
          http://www.apache.org/licenses/LICENSE-2.0
  
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
-->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_height="?android:attr/listPreferredItemHeight"
    android:layout_width="match_parent"
    android:background="@color/white"
    android:orientation="horizontal">
    <ImageView
        android:layout_marginLeft="10dp"
        android:layout_width="55dp"
        android:layout_height="match_parent"
        android:src="@drawable/app_notes"

        />
    <LinearLayout android:orientation="vertical"
        android:layout_height="match_parent"
        android:layout_width="wrap_content"
        android:layout_weight="1"
        >
        <TextView xmlns:android="http://schemas.android.com/apk/res/android"
            android:id="@android:id/text1"
            android:layout_width="match_parent"
            android:textColor="@color/noteBG"
            android:layout_height="35dp"
            android:textAppearance="?android:attr/textAppearanceLarge"
            android:textSize="22dp"
            android:gravity="center_vertical"
            android:paddingLeft="10dp"
            android:paddingTop="3dp"
            android:singleLine="true"
            />
        <TextView xmlns:android="http://schemas.android.com/apk/res/android"
            android:id="@+id/time1"
            android:layout_width="match_parent"
            android:paddingLeft="10dp"
            android:layout_height="20dp"
            android:textSize="18dp"
            android:singleLine="true"
            />
    </LinearLayout>
</LinearLayout>

```

每一栏的演示效果如下图

![1](https://img-blog.csdnimg.cn/20200522130441189.png)

接着在NotesList中的onCreate函数读取数据时增加获取时间属性，把时间值放到新添的textview中：

```java
    //修改PROJECTION
	private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, //TODO 修改：列表读取title时多读取时间戳属性
    };

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(NotePad.Notes.CONTENT_URI);
        }

        getListView().setOnCreateContextMenuListener(this);

        Cursor cursor = managedQuery(
            getIntent().getData(),            // Use the default content URI for the provider.
            PROJECTION,                       // Return the note ID and title for each note.
            null,                             // No where clause, return all records.
            null,                             // No where clause, therefore no where column values.
            NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
        );

		//绑定的属性名
        String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE , NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE } ;

        // The view IDs that will display the cursor columns, initialized to the TextView in
        //绑定的textview
        int[] viewIDs = { android.R.id.text1, R.id.time1 };

        // Creates the backing adapter for the ListView.
        adapter = new SimpleCursorAdapter(
                      this,                             // The Context for the ListView
                      R.layout.noteslist_item,          // Points to the XML for a list item
                      cursor,                           // The cursor to get items from
                      dataColumns,
                      viewIDs
              );

        // Sets the ListView's adapter to be the cursor adapter that was just created.
        setListAdapter(adapter);
    }
```

效果：

![2](https://img-blog.csdnimg.cn/20200522132620906.gif)



### title查询功能（动态查询）

如果上面的结构懂了之后查询功能就很简单了，原理就是在NotesList页面获取搜索关键词然后在查询时使用sql 的like添加查询条件，模糊插叙带有关键词的title，所以只要两步，先要有一个用户输入框，然后根据用户输入模糊查询。

搜索输入框使用SearchView，为他实现setOnQueryTextListener监听事件onQueryTextChange，每当搜索框内容改变时就做一次条件查询，查询条件设置为" where title like '%搜索框内容%' "，如此实现动态的模糊搜索。SearchView的简单设置都在onCreateOptionsMenu内实现

```java
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_options_menu, menu);
        //TODO 添加：action bar搜索框
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) searchItem.getActionView();
        //设置是否显示搜索框展开时的提交按钮
        mSearchView.setSubmitButtonEnabled(false);
        //设置输入框提示语
        mSearchView.setQueryHint("标题搜索");
        //SearchView设置监听
        /**SearchView设置监听**/
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
            //    Toast.makeText(NextActivity.this, "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(NextActivity.this, "Open", Toast.LENGTH_SHORT).show();
            }
        });
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
             //   Log.d(TAG, "TextSubmit : " + s);
            //    LogUtil.e(NextActivity.class, "TextSubmit : " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                /* Performs a managed query. The Activity handles closing and requerying the cursor
                 * when needed.
                 *
                 * Please see the introductory note about performing provider operations on the UI thread.
                 */
                String mslection = new String (NotePad.Notes.COLUMN_NAME_TITLE+" LIKE '%"+s+"%'"  );
                String[] mArgs = new String[] {  s  };
                Cursor cursor = managedQuery(
                        getIntent().getData(),            // Use the default content URI for the provider.
                        PROJECTION,                       // Return the note ID and title for each note.
                        mslection,                             // No where clause, return all records.
                        null,                             // No where clause, therefore no where column values.
                        NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
                );
                Log.e(TAG, "查询到条数 --> " + cursor.getCount());

                /*
                 * The following two arrays create a "map" between columns in the cursor and view IDs
                 * for items in the ListView. Each element in the dataColumns array represents
                 * a column name; each element in the viewID array represents the ID of a View.
                 * The SimpleCursorAdapter maps them in ascending order to determine where each column
                 * value will appear in the ListView.
                 */

                // The names of the cursor columns to display in the view, initialized to the title column
                String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE , NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE } ;

                // The view IDs that will display the cursor columns, initialized to the TextView in
                // noteslist_item.xml
                int[] viewIDs = { android.R.id.text1, R.id.time1 };

                // Creates the backing adapter for the ListView.
                adapter.changeCursor(cursor);

                // Sets the ListView's adapter to be the cursor adapter that was just created.
                setListAdapter(adapter);
                Log.d(TAG, "TextChange --> " + s);
             //   LogUtil.e(NextActivity.class, "TextChange --> " + s);
                return false;
            }
        });
        // 配置SearchView的属性
        // Generate any additional actions that can be performed on the
        // overall list.  In a normal install, there are no additional
        // actions found here, but this allows other applications to extend
        // our menu with their own actions.
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, NotesList.class), null, intent, 0, null);
        return super.onCreateOptionsMenu(menu);
    }
```

效果如下图：

![3](https://img-blog.csdnimg.cn/20200522130519486.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21hc3Rlcl9fYm95,size_16,color_FFFFFF,t_70)

动态演示：

![4](https://img-blog.csdnimg.cn/20200522132804165.gif)













