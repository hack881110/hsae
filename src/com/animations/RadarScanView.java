package com.animations;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.hsae.R;


/**
 * Created by 郭攀峰 on 2015/8/19.
 */
public class RadarScanView extends View
{
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    private int defaultWidth;
    private int defaultHeight;
    private int start;
    private int centerX;
    private int centerY;
    private int radarRadius;
    private int circleColor = Color.parseColor("#468EFF00");  //圆圈的颜色
    private int radarColor = Color.parseColor("#99f86860"); // 雷达的颜色
    private int tailColor = Color.parseColor("#50f86860");  //扫描的颜色

    private Paint mPaintCircle;
    private Paint mPaintRadar;
    private Matrix matrix;
    private boolean isScan = true;
    private Handler handler = new Handler();
    private Runnable run = new Runnable()
    {
        @Override
        public void run()
        {
            //通过这个线程来进行刷新雷达的角度 ，这样就让雷达动起来了
            	if (start == 360 || start > 360) {  //这个是要旋转的角度，360度意思是回到了原点
    				start = 0;
    			}
                start += 2;
//                Log.e("start_size", start + "");
                matrix = new Matrix();
                matrix.postRotate(start, centerX, centerY);  //旋转
                postInvalidate();    //刷新界面
                handler.postDelayed(run, 10);	
        }
    };

    public RadarScanView(Context context)
    {
        super(context);
        init(null, context);
    }

    public RadarScanView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, context);
    }

    public RadarScanView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        //centerX  centerY 使用来确定要绘制的圆的中心点
        centerX = w / 2;
        centerY = h / 2;
        radarRadius = Math.min(w, h);   //半径
    }

    private void init(AttributeSet attrs, Context context)
    {
        if (attrs != null)
        {
            TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RadarScanView);
            circleColor = ta.getColor(R.styleable.RadarScanView_circleColor2, circleColor);
            radarColor = ta.getColor(R.styleable.RadarScanView_radarColor, radarColor);
            tailColor = ta.getColor(R.styleable.RadarScanView_tailColor, tailColor);
            ta.recycle();
        }

        initPaint();
        //得到当前屏幕的像素宽高

        defaultWidth = dip2px(context, DEFAULT_WIDTH);
        defaultHeight = dip2px(context, DEFAULT_HEIGHT);

        matrix = new Matrix();
        handler.post(run);
    }
    /**
     * 设置画笔
     */
    private void initPaint()
    {
        mPaintCircle = new Paint();
        mPaintCircle.setColor(circleColor);
        mPaintCircle.setAntiAlias(true);//抗锯齿
        mPaintCircle.setStyle(Paint.Style.STROKE);//设置实心
        mPaintCircle.setStrokeWidth(2);//画笔宽度

        mPaintRadar = new Paint();
        mPaintRadar.setColor(radarColor);
        mPaintRadar.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int resultWidth = 0;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY)
        {
            resultWidth = sizeWidth;
        }
        else
        {
            resultWidth = defaultWidth;
            if (modeWidth == MeasureSpec.AT_MOST)
            {
                resultWidth = Math.min(resultWidth, sizeWidth);
            }
        }

        int resultHeight = 0;
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeHeight == MeasureSpec.EXACTLY)
        {
            resultHeight = sizeHeight;
        }
        else
        {
            resultHeight = defaultHeight;
            if (modeHeight == MeasureSpec.AT_MOST)
            {
                resultHeight = Math.min(resultHeight, sizeHeight);
            }
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);


        canvas.drawCircle(centerX, centerY, radarRadius / 7, mPaintCircle);
        canvas.drawCircle(centerX, centerY, radarRadius / 4, mPaintCircle);
        canvas.drawCircle(centerX, centerY, radarRadius / 3, mPaintCircle);
        canvas.drawCircle(centerX, centerY, 3 * radarRadius / 7, mPaintCircle);


        Shader shader = new SweepGradient(centerX, centerY, Color.TRANSPARENT, tailColor);
        mPaintRadar.setShader(shader);
        canvas.concat(matrix);
        canvas.drawCircle(centerX, centerY, 3 * radarRadius / 7, mPaintRadar);
    }

    private int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}