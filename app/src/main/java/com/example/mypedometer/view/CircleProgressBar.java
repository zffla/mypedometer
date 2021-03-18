package com.example.mypedometer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.mypedometer.R;
import com.example.mypedometer.utils.LogUtils;

public class CircleProgressBar extends View {

    private int mRadius;
    private int mArcWidth;
    //进度
    private double mProgress;
    //是否重置
    private boolean isReset=false;

    private Paint mPathPaint;
    private Paint mFillPaint;

    private SweepGradient mSweepGradient;

    private RectF mArcRect;

    //梯度渐变的填充颜色
//    private int[] arcColors = new int[]{0xFF02C016, 0xFF3DF346, 0xFF40F1D5, 0xFF02C016};
    private int[] arcColors = new int[]{0xFF87CEEB,0xFF6495ED,0xFF4682B4,0xFF0099CC,0xFF33b5e5,0xFF87CEFA};
    //灰色轨迹0xFFF0EEDF
    private static final int PATH_COLOR = 0xFFF5F5F5;
//    private static final int PATH_COLOR =0x02C016;
    private static int BORDER_COLOR = 0xFFD2D1C4;

    // 指定了光源的方向和环境光强度来添加浮雕效果
    private EmbossMaskFilter emboss = null;
    // 设置光源的方向
    float[] direction = new float[]{1, 1, 1};
    //设置环境光亮度
    float light = 0.4f;
    // 选择要应用的反射等级
    float specular = 6;
    // 向 mask应用一定级别的模糊
    float blur = 3.5f;
//
    //指定了一个模糊的样式和半径来处理 Paint 的边缘
    private BlurMaskFilter mBlur = null;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    /**
     * 初始化
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        //获取自定义属性
        mRadius = (int) array.getDimension(R.styleable.CircleProgressBar_radius, 120);
        mArcWidth = (int) array.getDimension(R.styleable.CircleProgressBar_arcWidth, 35);
        array.recycle();
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPathPaint = new Paint();
        // 设置是否抗锯齿
        mPathPaint.setAntiAlias(true);
        // 帮助消除锯齿
        mPathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setDither(true);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);

        mFillPaint = new Paint();
        // 设置是否抗锯齿
        mFillPaint.setAntiAlias(true);
        // 帮助消除锯齿
        mFillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        mFillPaint.setStyle(Paint.Style.STROKE);
        mFillPaint.setDither(true);
        mFillPaint.setStrokeJoin(Paint.Join.ROUND);
//        this.oval = new RectF();
        emboss = new EmbossMaskFilter(this.direction, this.light, this.specular, this.blur);
//        mBlur = new BlurMaskFilter(10.0F, BlurMaskFilter.Blur.NORMAL);
        mSweepGradient = new SweepGradient((float) (mRadius / 2), (float) (mRadius / 2), this.arcColors, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcRect = new RectF(0, 0, measureWidth(), measureHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSize, measureWidth() + getPaddingLeft() + getPaddingRight());
                break;
            case MeasureSpec.UNSPECIFIED:
                width = measureWidth() + getPaddingLeft() + getPaddingRight();
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, measureHeight() + getPaddingBottom() + getPaddingTop());
                break;
            case MeasureSpec.UNSPECIFIED:
                height = measureHeight() + getPaddingBottom() + getPaddingTop();
                break;
        }

        int measure = Math.min(width, height);
        setMeasuredDimension(measure, measure);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measure = getMeasuredHeight();
        //先画一个路径
        mPathPaint.setColor(PATH_COLOR);
        mPathPaint.setStrokeWidth(mArcWidth);
        //添加浮雕效果
        mPathPaint.setMaskFilter(emboss);
        canvas.drawCircle(measure / 2, measure / 2, measure/2 - mPathPaint.getStrokeWidth()/2, mPathPaint);
        //路径内外再画两个圆
        mPathPaint.setStrokeWidth(0.5f);
        mPathPaint.setColor(BORDER_COLOR);
        canvas.drawCircle(measure / 2, measure / 2, measure/2 - mPathPaint.getStrokeWidth() / 2, mPathPaint);
        canvas.drawCircle(measure / 2, measure / 2, measure/2 - mArcWidth - mPathPaint.getStrokeWidth() / 2, mPathPaint);
        //画进度弧
        mFillPaint.setShader(mSweepGradient);
        mFillPaint.setStrokeWidth(mArcWidth);
//        mFillPaint.setMaskFilter(mBlur);
        mFillPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcRect.set(getPaddingLeft() + mArcWidth / 2, getPaddingTop() + mArcWidth / 2, measure - getPaddingRight() - mArcWidth / 2, measure - getPaddingBottom() - mArcWidth / 2);
        canvas.drawArc(mArcRect, -90f, (float) (mProgress * 360), false, mFillPaint);
    }

    private int measureHeight() {
        return mRadius * 2;
    }

    private int measureWidth() {
        return mRadius * 2;
    }

    /**
     * 设置进度
     * @param progress 当前进度
     */
    public void setProgress(double progress){
        //progress∈[0,1]
        mProgress=progress;
        invalidate();
    }


    /**
     * 重置进度
     */
    public void reset(){
        isReset=true;
        mProgress=0;
        invalidate();
    }
}
