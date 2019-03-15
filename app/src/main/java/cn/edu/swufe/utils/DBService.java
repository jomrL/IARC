package cn.edu.swufe.utils;
import android.util.Log;

import java.sql.*;

import cn.edu.swufe.bean.User;

public class DBService {
    String TAG="DBService";
    private Connection conn=null; //打开数据库对象
    private PreparedStatement ps=null;//操作整合sql语句的对象
    private ResultSet rs=null;//查询结果的集合

    //DBService 对象
    public static DBService dbService=null;

    /**
     * 构造方法 私有化
     * */

    private DBService(){

    }

    /**
     * 获取MySQL数据库单例类对象
     * */

    public static DBService getDbService(){
        if(dbService==null){
            dbService=new DBService();
        }
        return dbService;
    }

    /**
     * 向数据库插入数据   增
     * */

    public void insertUserData(User user){
        if(user!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result=0;
                    //获取链接数据库对象
                    conn= DBOpenHelper.getConn();
                    //MySQL 语句
                    String sql="INSERT INTO mail_table (finger_slides,touch_times,read_duration,average_touch_duration," +
                            "mail_height,mail_width,left_flag,right_flag,up_flag,down_flag," +
                            "average_location_x,average_location_y,average_sliding_distance," +
                            "average_finger_pressure,average_sensor_x,average_sensor_y,average_sensor_z," +
                            "QQ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//18个
                    try {
                        boolean closed=conn.isClosed();
                        if((conn!=null)&&(!closed)){
                            ps= (PreparedStatement) conn.prepareStatement(sql);
                            int move_time=user.getMove_time();//触摸手指移动次数（剩下的是单机次数）
                            int click_time=user.getClick_time();//记录触摸屏幕次数
                            long alive_time=user.getAlive_time();//记录整篇文章的阅读时间
                            float average_touch_distance=user.getAverage_touch_distance();//平均滑动距离
                            float average_touch_time=user.getAverage_touch_time();//平均每次触摸时间
                            int measuredHeight=user.getMeasuredHeight();//文章高度
                            int measureWeight=user.getMeasureWeight();//文章宽度
                            int left_f=user.getLeft_f();//左倾标志
                            int right_f=user.getRight_f();//右倾标志
                            int up_f=user.getUp_f();//上倾标志
                            int down_f=user.getDown_f();//下倾标志
                            float sensor_average_x=user.getSensor_average_x();
                            float sensor_average_y=user.getSensor_average_y();
                            float sensor_average_z=user.getSensor_average_z();
                            float click_average_x=user.getClick_average_x();
                            float click_average_y=user.getClick_average_y();
                            float click_average_size=user.getClick_average_size();//平均指压范围
                            String qq=user.getQQ();
//                            Log.i(TAG, "run: movetime"+move_time);
//                            Log.i(TAG, "run: click_time"+click_time);
//                            Log.i(TAG, "run: alive_time"+alive_time);
//                            Log.i(TAG, "run: average_touch_time"+average_touch_time);
//                            Log.i(TAG, "run: measuredHeight"+measuredHeight);
//                            Log.i(TAG, "run: measureWeight"+measureWeight);
//                            Log.i(TAG, "run: left_f"+left_f);
//                            Log.i(TAG, "run: right_f"+right_f);
//                            Log.i(TAG, "run: up_f"+up_f);
//                            Log.i(TAG, "run: down_f"+down_f);
//                            Log.i(TAG, "run: click_average_x"+click_average_x);
//                            Log.i(TAG, "run: click_average_y"+click_average_y);
//                            Log.i(TAG, "run: average_touch_distance"+average_touch_distance);
//                            Log.i(TAG, "run: click_average_size"+click_average_size);
//                            Log.i(TAG, "run: qq"+qq);
//                            Log.i(TAG, "run: sensor_average_x"+sensor_average_x);
//                            Log.i(TAG, "run: sensor_average_y"+sensor_average_y);
//                            Log.i(TAG, "run: sensor_average_z"+sensor_average_z);

                            ps.setInt(1,move_time);
                            ps.setInt(2,click_time);
                            ps.setLong(3,alive_time);
                            ps.setFloat(4,average_touch_time);
                            ps.setInt(5,measuredHeight);
                            ps.setInt(6,measureWeight);
                            ps.setInt(7,left_f);
                            ps.setInt(8,right_f);
                            ps.setInt(9,up_f);
                            ps.setInt(10,down_f);
                            ps.setFloat(11,click_average_x);
                            ps.setFloat(12,click_average_y);
                            ps.setFloat(13,average_touch_distance);
                            ps.setFloat(14,click_average_size);
                            ps.setFloat(15,sensor_average_x);
                            ps.setFloat(16,sensor_average_y);
                            ps.setFloat(17,sensor_average_z);
                            ps.setString(18,qq);

                            result=ps.executeUpdate();//返回1 执行成功
                        }
                    } catch (SQLException e) {
                        Log.i("", "insertUserData: "+"执行失败");
                        e.printStackTrace();
                    }
                    DBOpenHelper.closeAll(conn,ps);//关闭相关操作
                }
            }).start();

        }

    }

}
