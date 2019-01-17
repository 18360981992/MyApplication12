package com.ifeng_tech.demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    private TextView tuodong;
    private LinearLayout bianhua;
    public int sy;
    public int bianhua_height;
    public int t_yuan;
    public int b_yuan;
    public int tuodong_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    int type=0;
    @Override
    protected void onResume() {
        super.onResume();
        final LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) bianhua.getLayoutParams(); //取控件textView当前的布局参数
        tuodong.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                        sy = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                        int y = (int) event.getRawY();
                        // 获取手指移动的距离
                        int dy = y - sy;
                        Log.i("jba","dy=="+dy);
                        // 判断手势是滑动的方向
                        if(dy>0){ // 向下
                            type=1;
                        }else if(dy<0){ // 向上
                            type=2;
                        }

                        // 得到textView最开始的各顶点的坐标
                        int l = tuodong.getLeft();
                        int r = tuodong.getRight();
                        int t = tuodong.getTop();
                        int b = tuodong.getBottom();
                        bianhua_height=bianhua_height+dy;
                        Log.i("jba","bianhua_height=="+bianhua_height);
                        if(bianhua_height<0){
                            bianhua.setVisibility(View.VISIBLE);
                            linearParams.height = Math.abs(bianhua_height);
                            bianhua.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                        }else if(bianhua_height>=0){
                            bianhua.setVisibility(View.INVISIBLE); // 这里的隐藏显示只为了实现对整体效果的一点点小优化，是它更流畅
                        }

                        tuodong.layout(l , t + dy, r , b + dy);// 更改textView在窗体的位置

                        sy = (int) event.getRawY();  // 获取移动后的位置 用来实时更新当前位置  这行代码非常重要
                        break;
                    case MotionEvent.ACTION_UP:
                        // 得到imageView最开始的各顶点的坐标
                        int lup = tuodong.getLeft();
                        int rup = tuodong.getRight();
                        int tup = tuodong.getTop();
                        int bup = tuodong.getBottom();
                        Log.i("jba","tup=="+tup+",bup=="+bup);
                        // 限制手势的活动范围
                        if(bup >= t_yuan){ // 从最底部拖动 是否到达活动范围内
                            tuodong.layout(lup , t_yuan, rup , b_yuan);
                            bianhua_height=0;
                            linearParams.height = bianhua_height;
                            bianhua.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                        }else if(tup <= 600+tuodong_height){   // 从最顶部拖动  是否到达活动范围内  注：这里的600只是个量词  大家可以根据自己的需求进行更改 比如屏幕高度的百分之多少
                            tuodong.layout(lup ,600, rup , tuodong_height+600);
                            bianhua_height = tuodong_height - b_yuan + 600;
                            linearParams.height =Math.abs(bianhua_height);
                            bianhua.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                        }else if( bup<t_yuan && bup>600+tuodong_height && type==2){ // 向上 并且活动范围在最大和最小之间
                            tuodong.layout(lup ,600, rup , tuodong_height+600);
                            bianhua_height = tuodong_height - b_yuan + 600;
                            linearParams.height =Math.abs(bianhua_height);
                            bianhua.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                        }else if(tup < t_yuan-tuodong_height && tup > 600+tuodong_height && type==1){ // 向下  并且活动范围在最大和最小之间
                            tuodong.layout(lup , t_yuan, rup , b_yuan);
                            bianhua_height=0;
                            linearParams.height = bianhua_height;
                            bianhua.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void initView() {
        tuodong = (TextView) findViewById(R.id.tuodong);
        bianhua = (LinearLayout) findViewById(R.id.bianhua);

        //通过设置监听来获取 微弹窗 控件的高度
        bianhua.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                bianhua.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获取控件的初始高度  
                bianhua_height = bianhua.getMeasuredHeight();
                tuodong_height = tuodong.getMeasuredHeight();
                t_yuan = tuodong.getTop();
                b_yuan = tuodong.getBottom();
                Log.i("jba","bianhua_height=="+bianhua_height+",tuodong_height=="+tuodong_height);
                Log.i("jba","t_yuan=="+t_yuan+",b_yuan=="+b_yuan);
            }
        });

    }

}
