package cn.edu.swufe.utils;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBOpenHelper {

    private static String driver = "com.mysql.jdbc.Driver";//MySQL 驱动

    /**
     * 连接数据库
     * */

    public static Connection getConn(){
        Connection conn=null;
        try {
            Class.forName(driver);//获取MYSQL驱动
            conn = DriverManager.getConnection("jdbc:mysql://120.79.169.82:3306/mail_data?user=root&password=123456&useUnicode=true&characterEncoding=utf-8");
            Log.i("DBOpenHelper", "getConn: "+"成功连接mysql数据库");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return conn;
    }

    /**
     * 关闭数据库
     * */

    public static void closeAll(Connection conn, PreparedStatement ps){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 关闭数据库
     * */

    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}