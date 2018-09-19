package com.xue.yado.wy_news.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/8/22.
 */

public class ViewPageIndicate extends LinearLayout {

    private int mTop; // 指示符的top
    private int mLeft; // 指示符的left
    private float mWidth; // 指示符的width
   // private int mHeight = 15; // 指示符的高度，固定了
   private float mHeight;
    private int mColor; // 指示符的颜色
    private int mChildCount; // 子item的个数，用于计算指示符的宽度
    private Paint mPaint; // 画指示符的paint
    private float mTransactionX = 50;//指示符偏移量
    private Path path;




    public ViewPageIndicate(Context context) {
        super(context);
        initData();
    }

    public ViewPageIndicate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.rgb(234,111,90));
        mPaint.setAntiAlias(true);

    }

    /**
     * 子View加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 获取子item的个数
        mChildCount = getChildCount();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mTop = getMeasuredHeight(); // 测量的高度即指示符的顶部位置
        int width = getMeasuredWidth(); // 获取测量的总宽度
        //float height = mTop + mHeight; // 重新定义一下测量的高度
       // mWidth = (int) (width / mChildCount-2*mTransactionX); // 指示符的宽度为总宽度/item的个数

        //三角形宽度
        mWidth = width/mChildCount/mChildCount;
        mHeight = (float) (mWidth* Math.sqrt(3)/2)/3;
        int height = (int) (mTop + mHeight); // 重新定义一下测量的高度
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    public void scoll(int position,float offset){
        //mLeft = (int) (((position + offset) * (mWidth+2*mTransactionX))+mTransactionX);;
        mLeft = (int) ((position+offset)*(mWidth+2*mWidth)+mWidth);
        invalidate();

    }



    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        //Rect rect = new Rect(mLeft, mTop, mLeft + mWidth, mTop + mHeight);
       // canvas.drawRect(rect, mPaint); // 绘制该矩形
       // canvas.translate(mLeft,mHeight+mTop);

        path = new Path();
        path.moveTo(mLeft,mHeight+mTop);

        path.lineTo((float) (mLeft + (0.5 * mWidth)),mTop);
        path.lineTo(mLeft+mWidth,mHeight+mTop);
        path.close();
        canvas.drawPath(path,mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }



}
