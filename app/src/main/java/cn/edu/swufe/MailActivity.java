package cn.edu.swufe;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import cn.edu.swufe.bean.Email;
import cn.edu.swufe.bean.User;
import cn.edu.swufe.qqmailapp.R;
import cn.edu.swufe.utils.DBService;

public class MailActivity extends AppCompatActivity {
    String TAG="MainActivity";
    private float downX,downY;
    WebView v1;
    Mobile mobile = new Mobile();
    int move_time=0;//触摸手指移动次数（剩下的是单机次数）
    int click_time=0;//记录触摸屏幕次数
    long alive_time=0l;//记录整篇文章的阅读时间
    float average_touch_distance=0;//平均滑动距离
    long start_time=0l;//阅读开始时间
    long end_time=0l;//阅读结束时间
    float average_touch_time=0;//平均每次触摸时间
    int measuredHeight=0;//文章高度
    int measureWeight=0;//文章宽度
    int left_f=0;//左倾标志
    int right_f=0;//右倾标志
    int up_f=0;//上倾标志
    int down_f=0;//下倾标志
    float sensor_average_x=0f;
    float sensor_average_y=0f;
    float sensor_average_z=0f;
    long sensor_count=0l;
    float click_average_x=0f;
    float click_average_y=0f;
    float click_average_size=0f;//平均指压范围
    long click_count=0l;
    String qq;
    SensorManager sm;

    @Override
    protected void onResume() {
        super.onResume();
        if(sm!=null){
            //注册监听器
            sm.registerListener(sensorEventListener,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(sm!=null){
            //取消监听器
            sm.unregisterListener(sensorEventListener);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: 滑动次数"+click_time);
        end_time=System.currentTimeMillis();
        if(click_time!=0){
            average_touch_time=average_touch_time/click_time;
        }else{
            average_touch_time=0l;
        }
        if(move_time!=0){
            average_touch_distance=average_touch_distance/move_time;
        }else{
            average_touch_distance=0;
        }
        alive_time=end_time-start_time;
        sensor_average_x=sensor_average_x/sensor_count;
        sensor_average_y=sensor_average_y/sensor_count;
        sensor_average_z=sensor_average_z/sensor_count;
        if(click_count==0){
            click_average_size=0;
            click_average_x=0;
            click_average_y=0;
        }else{
            click_average_x=click_average_x/click_count;
            click_average_y=click_average_y/click_count;
            click_average_size=click_average_size/click_count;
        }

        User user=new User();
        user.setMove_time(move_time);
        user.setClick_time(click_time);
        user.setAlive_time(alive_time);
        user.setAverage_touch_time(average_touch_time);
        user.setMeasuredHeight(measuredHeight);
        user.setMeasureWeight(measureWeight);
        user.setDown_f(down_f);
        user.setLeft_f(left_f);
        user.setRight_f(right_f);
        user.setUp_f(up_f);
        user.setClick_average_x(click_average_x);
        user.setClick_average_y(click_average_y);
        user.setAverage_touch_distance(average_touch_distance);
        user.setClick_average_size(click_average_size);
        user.setSensor_average_x(sensor_average_x);
        user.setSensor_average_y(sensor_average_y);
        user.setSensor_average_z(sensor_average_z);
        user.setQQ(qq);
        DBService.dbService.insertUserData(user);
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=ev.getX();
                downY=ev.getY();
                click_average_x+=downX;
                click_average_y+=downY;
                click_average_size+=ev.getSize();
                click_count++;

                break;
            case MotionEvent.ACTION_MOVE:
                click_average_x+=ev.getX();
                click_average_y+=ev.getY();
                click_average_size+=ev.getSize();
                click_count++;
                break;
            case MotionEvent.ACTION_UP:
                click_average_x+=ev.getX();
                click_average_y+=ev.getY();
                click_average_size+=ev.getSize();
                click_count++;
                if(ev.getX()!=downX && ev.getY()!=downY){
                    move_time++;
                    average_touch_distance+=Math.sqrt(Math.pow(ev.getX()-downX,2)+Math.pow(ev.getY()-downY,2));
                }
                click_time++;
                Log.i(TAG, "ACTION:按下结束时间 "+ev.getEventTime());
                average_touch_time+=ev.getEventTime()-ev.getDownTime();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        DBService.getDbService();//获得DBService实例
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        v1=(WebView) findViewById(R.id.mail_content_info);
        start_time=System.currentTimeMillis();
        String mimetype=getIntent().getStringExtra("mimitype");
        qq=getIntent().getStringExtra("qq");
        Email email=(Email)getIntent().getSerializableExtra("email");
        String from =email.getFrom();
        String subject=email.getSubject();
        String content=email.getContent();
        TextView v=(TextView)findViewById(R.id.mail_from_info);
        v.setText(from+"");
        v=(TextView)findViewById(R.id.mail_subject_info);
        v.setText(subject+"");


        v1.setWebViewClient(mClient);
        WebSettings settings=v1.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        settings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        settings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        settings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        v1.addJavascriptInterface(mobile, "mobile");
        String head="<html><head>" +
                "<meta charset=\"utf-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\"/>";
        String tail="</head></html>";
        if(content.indexOf("<meta")!=-1){
            String s="<meta charset=\"utf-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\"/>";
            StringBuffer content1=new StringBuffer(content);
            content1.insert(content.indexOf("<meta"),s);
            content=content1.toString();
        }
        if(content.indexOf("<html")!=-1){
            v1.loadData(content, mimetype+";charset=utf-8","utf-8");
        }else{
            v1.loadData(head+content+tail, mimetype+";charset=utf-8","utf-8");
        }
    }
    private class Mobile {
        @JavascriptInterface
        public void onGetWebContentHeight() {
            //重新调整webview高度
            v1.post(() -> {
                v1.measure(0, 0);
                measuredHeight = v1.getMeasuredHeight();
                measureWeight =v1.getMeasuredWidth();
            });


        }
    }


    private WebViewClient mClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            mobile.onGetWebContentHeight();
        }
    };
    private SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            sensor_count++;
            float[] values=event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            sensor_average_x+=x;
            sensor_average_y+=y;
            sensor_average_z+=z;
            if(x>=1){
                left_f=1;
            }
            if(x<=-1){
                right_f=1;
            }
            if(y>=1){
                down_f=1;
            }
            if(y<=-1){
                up_f=1;
            }
//            Log.i(TAG, "onSensorChanged: "+x+","+y);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
