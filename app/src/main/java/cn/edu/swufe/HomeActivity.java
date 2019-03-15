package cn.edu.swufe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;


import cn.edu.swufe.bean.Email;
import cn.edu.swufe.qqmailapp.R;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<Email> mailslist = new ArrayList<Email>();
    private int headerViewHeight;
    /** 状态：下拉刷新 */
    private static final int STATE_PULL_TO_REFRESH = 0;
    /** 状态：松开刷新 */
    private static int finish = 0;
    /** 状态：正在刷新 */
    private static final int STATE_REFRESHING = 2;
    /** 当前状态 */
    private int currentState = STATE_PULL_TO_REFRESH;    // 默认是下拉刷新状态
    /** 正在加载更多 */
    private boolean loadingMore=true;
    private int flag=0;
    private String mimetype;
    Properties props;
    final String TAG="HomeActivity";
    Store store;
    Folder inbox;
    private Message[] messages=null;
    Thread thread=null;
    private SimpleAdapter myAdapter=null;
    private PullToRefreshListView lv_box;
    private StringBuffer bodyText = new StringBuffer();
    // key值数组，适配器通过key值取value，与列表项组件一一对应
    String[] froms = { "from", "subject", "time"};
    // 列表项组件Id 数组
    int[] tos = { R.id.tv_from, R.id.tv_subject, R.id.tv_sentdate};
    private ProgressDialog dialog;
    private String username;
    Session session;
    int flag1=0;
    private String pwd;
    private List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    private List<Map<String, String>> list1=new ArrayList<Map<String,String>>();
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==3){
                closeHeadOrFooter();
            }
            else if(msg.what==0){
                if(flag==2){
                    list.clear();
                    flag=0;
                }else{
                    flag=0;
                }
                for(Map<String,String> map:list1){
                    list.add(map);
                }
                list1.clear();
                dialog.dismiss();
                if(myAdapter==null){
                    Log.i(TAG, "handleMessage: "+"new Adapter");
                    myAdapter=new SimpleAdapter(HomeActivity.this,list,R.layout.my_list,froms,tos);
                    lv_box.setAdapter(myAdapter);
                }
                else{
                    myAdapter.notifyDataSetChanged();
                }
            }
            else{

            }

        };

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bdl=getIntent().getExtras();
        username=bdl.getString("name");
        Log.i("HomeActivity", "onCreate: name"+username);
        pwd=bdl.getString("pwd");
        initView();
        lv_box.setOnRefreshingListener(new PullToRefreshListView.OnRefreshingListener() {
            @Override
            public void onRefreshing() {
                // 下拉刷新的时候回调该方法，加载数据
                Log.i(TAG, "onRefreshing: "+loadingMore);
                finish=0;
                if(loadingMore==false){
                    flag1=1;
                    loadData();
                }

            }

            @Override
            public void onLoadMore() {
                // 上拉加载下一页数据的回调
                Log.i(TAG, "onLoadMore: "+loadingMore);
                if(loadingMore==false){
                    loadingMore=true;
                    loadMoreData();
                }

            }
        });
    }
    private void loadMoreData() {
        loadingMore=true;
        flag=1;
        if(finish==1){
            Log.i(TAG, "loadMoreData: "+finish);
            closeHeadOrFooter();
            loadingMore=false;
        }
    }

    private void loadData() {
        Log.i(TAG, "loadData: "+flag1);
        flag=2;
        loadingMore=true;
        mailslist.clear();
        while(flag1==0 || thread.isAlive()){
            try{
                Thread.sleep(1000);
            }catch (Exception es){
                es.printStackTrace();
            }
        }
        Log.i(TAG, "loadData:threadisAlive "+thread.isAlive());
        thread= getThread();
        thread.start();
    }

    @NonNull
    private Thread getThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                flag1=0;
                store=null;
                inbox = null;
                try {
                    store = session.getStore("imap");
                    Log.i("Infomation", "run: " + username + "," + pwd);
                    store.connect("imap.qq.com", username, pwd);
                    inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
                    FetchProfile profile = new FetchProfile();
                    profile.add(FetchProfile.Item.ENVELOPE);
                    messages = inbox.getMessages();
                    Message messages1[]=new Message[messages.length];
                    for(int i=0;i<messages.length;i++){
                        if(!messages[i].getFolder().isOpen()){
                            messages[i].getFolder().open(Folder.READ_ONLY);
                        }
                        messages1[i]=messages[messages.length-1-i];
                    }
                    messages=messages1;
                    Log.i("Length", "收件箱的邮件数：" + messages.length);
                    Log.i("test",messages.length+"");
                    for (int i = 0; i < messages.length; i++) {
                        Log.i(TAG, "runflag1: "+flag1);
                        if(flag1==1){
                            Log.i(TAG, "run: "+"结束");
                            break;
                        }
                        if(!messages[i].getFolder().isOpen()){
                            messages[i].getFolder().open(Folder.READ_ONLY);
                        }
                        try{
                            Map<String,String> map=new HashMap<String,String>();
                            //邮件发送者
                            String from = decodeText(messages[i].getFrom()[0].toString());
                            Log.i(TAG, "run: "+from);
                            InternetAddress ia = new InternetAddress(from);
                            //String mail_content=getAllMultipart(messages[i]);
                            Email email=new Email();
                            email.setFrom(ia.getPersonal()+'('+ia.getAddress()+')');
                            map.put("from",ia.getPersonal()+'('+ia.getAddress()+')');
                            email.setSubject( messages[i].getSubject());
                            map.put("subject",messages[i].getSubject());
                            bodyText.delete(0,bodyText.length());
                            getMailContent((Part)messages[i]);
                            email.setContent(getBodyText());
                            Date receivedDate = messages[i].getSentDate();
                            if (receivedDate == null){
                                email.setSentdata("");
                                map.put("time","");
                            }
                            else {
                                String pattern = "yyyy年MM月dd日 E HH:mm ";
                                email.setSentdata(new SimpleDateFormat(pattern).format(receivedDate));
                                map.put("time",new SimpleDateFormat(pattern).format(receivedDate));
                            }
                            list1.add(map);
                            mailslist.add(email);
                        }catch (Exception es){
                            es.printStackTrace();
                        }
                        if(((i+1)%10==0 &&i!=0 )||i==messages.length-1){
                            if(i==messages.length-1){
                                finish=1;
                            }
                            handler.sendEmptyMessage(3);
                            handler.sendEmptyMessage(0);

                            loadingMore=false;
                            Log.i(TAG, "run: "+"暂停线程"+loadingMore);
                            while(loadingMore==false){
                                try{
                                    Thread.sleep(1000);
                                    if(flag==2){
                                        loadingMore=true;
                                    }
                                    if(finish==1){
                                        break;
                                    }
                                }catch (Exception es){
                                    es.printStackTrace();
                                }
                            }
                            Log.i(TAG, "run: "+"线程启动获得新数据");
                        }
                    }

                    Log.i(TAG, "run: "+"结束1");
                    try {
                        inbox.close(false);
                        Log.i(TAG, "run: "+"close inbox");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        store.close();
                        Log.i(TAG, "run: "+"close store");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                catch(Exception e)
                {
                    Log.i("Error", "run: "+e);
                }
                finally {
                    flag1=0;
                }

            }
        });
    }

    private void initView() {
        lv_box = (PullToRefreshListView) findViewById(R.id.lv_box);
        lv_box.setOnItemClickListener(HomeActivity.this);
        dialog=new ProgressDialog(this);
        dialog.setMessage("正加载");
        dialog.show();
//        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.transport.protocol", "imap"); // 使用的协议
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");
        //以下步骤跟一般的JavaMail操作相同
        session = Session.getInstance(props,null);
        //请将红色部分对应替换成你的邮箱帐号和密码

        thread= getThread();
        thread.start();

    }
    public void closeHeadOrFooter(){
        Log.i(TAG, "closeHeadOrFooter: "+flag);
        if(flag==1){
            if(finish==1){
                TextView textView=(TextView)findViewById(R.id.tv_title);
                textView.setText("所有邮件已经加载完毕");
            }
            lv_box.onLoadmoreComplete();
            TextView textView=(TextView)findViewById(R.id.tv_title);
            textView.setText("正在加载");
        }else if(flag==2){
            TextView textView=(TextView)findViewById(R.id.tv_time);
            textView.setText("刷新成功");
            lv_box.onRefreshComplete();

        }else{

        }
    }
    protected static String decodeText(String text)
            throws UnsupportedEncodingException {
        if (text == null)
            return null;
        if (text.startsWith("=?GB") || text.startsWith("=?gb"))
            text = MimeUtility.decodeText(text);
        else
            text = new String(text.getBytes("ISO8859_1"));
        return text;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: "+"position="+position+"id="+id+"mailLength"+mailslist.size());
        Intent intent = new Intent(HomeActivity.this, MailActivity.class);
        Email email=(Email)mailslist.get(Integer.parseInt(String.valueOf(id)));
        intent.putExtra("email",email);
        intent.putExtra("mimitype",mimetype);
        intent.putExtra("qq",username);
        startActivity(intent);
    }
    /**

     * 　*　获得邮件正文内容 　

     */

    public String getBodyText() {

        return bodyText.toString();

    }
    /**

     * 　　*　解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件

     * 　　*　主要是根据MimeType类型的不同执行不同的操作，一步一步的解析 　　

     */



    public void getMailContent(Part part) throws Exception {



        String contentType = part.getContentType();

        // 获得邮件的MimeType类型

        int nameIndex = contentType.indexOf("name");

        boolean conName = false;

        if (nameIndex != -1) {

            conName = true;

        }

        if (part.isMimeType("text/plain") && conName == false) {
            mimetype="text/plain";
            // text/plain 类型

            bodyText.append((String) part.getContent());

        } else if (part.isMimeType("text/html") && conName == false) {
            mimetype="text/html";
            // text/html 类型

            bodyText.append((String) part.getContent());

        } else if (part.isMimeType("multipart/*")) {

            // multipart/*
            mimetype="text/html";
            Multipart multipart = (Multipart) part.getContent();

            int counts = multipart.getCount();

            for (int i = 0; i < counts; i++) {

                getMailContent(multipart.getBodyPart(i));

            }

        } else if (part.isMimeType("message/rfc822")) {
            mimetype="text/html";
            // message/rfc822

            getMailContent((Part) part.getContent());

        } else {
            mimetype="text/html";
        }

    }



}
