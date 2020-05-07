## 实验4：Intent

#### 第一部分：用webview实现一个自己的简单的浏览器页面：

在工程中新建一个模型：MyWebView的app

在main.xml中定义一个满屏的WebView组件

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >
    <WebView
        android:id="@+id/web_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

重点是配置AndroidManifest.xml中的activity和intent-filter：

我新建了一个activity然后其中设置一个能接收得到网址请求的intent-filter,主要设置了action，category，和category这三个属性（至少设置这几个）。

```xml
		<activity android:name=".MyWebView">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <!--
                每一个通过startActivity()方法发出的隐式Intent都至少有一个category，
                就是 "android.intent.category.DEFAULT",所以至少必须加category.DEFAUL
                然后还需再加一个BROWSABLE类别
                -->
                <category android:name="android.intent.category.DEFAULT"/>
                <category android:name="android.intent.category.BROWSABLE"/>
                <!--
                接收数据http和https的数据类型
                -->
                <category android:scheme="http"></data>
                <data android:scheme="https"></data>
            </intent-filter>
        </activity>
```

还有一点要注意的是：manifest中需要设置一下uses-permission属性，一开始我没有设置时运行app的时候网页报错,要配置如下属性：

```xml
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
```

然接着解释定义对应这个activity的操作类MyWebView：

```java
public class MyWebView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String targetUrl = getIntent().getData().toString();
        if(targetUrl==null)
            //若网址为空就直接去百度
            webView.loadUrl("http://www.baidu.com");
        else
            webView.loadUrl(targetUrl);
    }
}
```

直接运行测试一下，得到一个百度的主页：

![](https://img-blog.csdnimg.cn/20200507155542999.JPG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21hc3Rlcl9fYm95,size_16,color_FFFFFF,t_70)



#### 第二部分：获取URL地址并启动一个隐式的Intent：

先做一个简单的用于获取用户输入的页面：

```xml
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/loginForm"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- 标题栏 -->
    <TableRow
        android:background="@color/blue"
        android:paddingStart="12dp"
        android:paddingVertical="5dp">
        <TextView android:id="@+id/titleimage"
            android:layout_width="match_parent"
            android:layout_gravity="center"
            android:text="my web searching"
            android:textSize="30sp"
            />
    </TableRow>

    <TableRow android:paddingStart="12dp">
        <!-- 输入用户名的文本框 -->
        <EditText android:id="@+id/target_url"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Input you URL"
            android:gravity="center"
            android:textSize="25sp"
            android:ems="14"
            android:selectAllOnFocus="true" />
    </TableRow>

    <!-- 水平的按钮 -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <Button
            android:layout_height="match_parent"
            android:layout_width="wrap_content"
            android:layout_weight="1"
            android:text="clear"
            android:textSize="22sp"
            android:onClick="clear"/>
        <Button
            android:layout_height="match_parent"
            android:layout_width="wrap_content"
            android:layout_weight="1"
            android:text="search"
            android:textSize="22sp"
            android:onClick="searching"/>
    </LinearLayout>
</TableLayout>
```

界面效果如下：

![](https://img-blog.csdnimg.cn/20200507155020829.JPG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21hc3Rlcl9fYm95,size_16,color_FFFFFF,t_70)

下一步就是重点：

在search按钮的点击事件中创建一个可选的intent，获取edittext的内容作为data的值

```java
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
```

到这里就可以运行测试了，在运行前要先运行一下第一部分的webview

运行后输入一个网址：

![](https://img-blog.csdnimg.cn/2020050715505684.JPG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21hc3Rlcl9fYm95,size_16,color_FFFFFF,t_70)

弹出一个可选择操作应用的dialog，选择我自定义的的MyWebView

![](https://img-blog.csdnimg.cn/20200507155109420.JPG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21hc3Rlcl9fYm95,size_16,color_FFFFFF,t_70)

跳转至MyWebView并且打开刚才所输入的网址：

![](https://img-blog.csdnimg.cn/202005071551495.JPG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L21hc3Rlcl9fYm95,size_16,color_FFFFFF,t_70)

实验完成！









