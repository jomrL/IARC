package cn.edu.swufe.bean;


public class User {
    private int move_time;//触摸手指移动次数（剩下的是单机次数）
    private int click_time;//记录触摸屏幕次数
    private long alive_time;//记录整篇文章的阅读时间
    private float average_touch_distance;//平均滑动距离
    private float average_touch_time;//平均每次触摸时间
    private int measuredHeight;//文章高度
    private int measureWeight;//文章宽度
    private int left_f;//左倾标志
    private int right_f;//右倾标志
    private int up_f;//上倾标志
    private int down_f;//下倾标志
    private float sensor_average_x;
    private float sensor_average_y;
    private float sensor_average_z;
    private float click_average_x;
    private float click_average_y;
    private float click_average_size;//平均指压范围
    private String QQ;

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public int getMove_time() {

        return move_time;
    }

    public void setMove_time(int move_time) {
        this.move_time = move_time;
    }

    public int getClick_time() {
        return click_time;
    }

    public void setClick_time(int click_time) {
        this.click_time = click_time;
    }

    public long getAlive_time() {
        return alive_time;
    }

    public void setAlive_time(long alive_time) {
        this.alive_time = alive_time;
    }

    public float getAverage_touch_distance() {
        return average_touch_distance;
    }

    public void setAverage_touch_distance(float average_touch_distance) {
        this.average_touch_distance = average_touch_distance;
    }

    public float getAverage_touch_time() {
        return average_touch_time;
    }

    public void setAverage_touch_time(float average_touch_time) {
        this.average_touch_time = average_touch_time;
    }

    public int getMeasuredHeight() {
        return measuredHeight;
    }

    public void setMeasuredHeight(int measuredHeight) {
        this.measuredHeight = measuredHeight;
    }

    public int getMeasureWeight() {
        return measureWeight;
    }

    public void setMeasureWeight(int measureWeight) {
        this.measureWeight = measureWeight;
    }

    public int getLeft_f() {
        return left_f;
    }

    public void setLeft_f(int left_f) {
        this.left_f = left_f;
    }

    public int getRight_f() {
        return right_f;
    }

    public void setRight_f(int right_f) {
        this.right_f = right_f;
    }

    public int getUp_f() {
        return up_f;
    }

    public void setUp_f(int up_f) {
        this.up_f = up_f;
    }

    public int getDown_f() {
        return down_f;
    }

    public void setDown_f(int down_f) {
        this.down_f = down_f;
    }

    public float getSensor_average_x() {
        return sensor_average_x;
    }

    public void setSensor_average_x(float sensor_average_x) {
        this.sensor_average_x = sensor_average_x;
    }

    public float getSensor_average_y() {
        return sensor_average_y;
    }

    public void setSensor_average_y(float sensor_average_y) {
        this.sensor_average_y = sensor_average_y;
    }

    public float getSensor_average_z() {
        return sensor_average_z;
    }

    public void setSensor_average_z(float sensor_average_z) {
        this.sensor_average_z = sensor_average_z;
    }



    public float getClick_average_x() {
        return click_average_x;
    }

    public void setClick_average_x(float click_average_x) {
        this.click_average_x = click_average_x;
    }

    public float getClick_average_y() {
        return click_average_y;
    }

    public void setClick_average_y(float click_average_y) {
        this.click_average_y = click_average_y;
    }

    public float getClick_average_size() {
        return click_average_size;
    }

    public void setClick_average_size(float click_average_size) {
        this.click_average_size = click_average_size;
    }


}
