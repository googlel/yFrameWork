package com.liy.today.recyclerview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.laiyifen.lyfframework.R;


/**
 * 作者：qiujie on 16/4/17
 * 邮箱：qiujie@laiyifen.com
 */
public class AutoHome extends View{
    private Bitmap backGroundBitmap;
    public Bitmap pointerBitmap;
    private int x;
    private int y;
    private Bitmap finalBackGroundBitmap;
    private Bitmap finalPointerBitmap;
    private float mCurrentProgress;

    public AutoHome(Context context) {
        super(context);
        init(context);
    }


    public AutoHome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoHome(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        backGroundBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.arrowbg));
        pointerBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.arrow));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureWidth(heightMeasureSpec));
        x = getMeasuredWidth();
        y = getMeasuredHeight();
        finalBackGroundBitmap = Bitmap.createScaledBitmap(backGroundBitmap, x, y, true);
        finalPointerBitmap = Bitmap.createScaledBitmap(pointerBitmap, x, y, true);
    }
    private int measureWidth(int widMeasureSpec){
        int result = 0;
        int size = MeasureSpec.getSize(widMeasureSpec);
        int mode = MeasureSpec.getMode(widMeasureSpec);
        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else{
            result = backGroundBitmap.getWidth();
            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

            canvas.drawBitmap(finalBackGroundBitmap,0,0,null);
            canvas.rotate(mCurrentProgress*3.6f,x/2,y/2);
            canvas.drawBitmap(finalPointerBitmap, 0, 0, null);
    }


    public void setCurrentProgress(float progress){
        mCurrentProgress = progress*100;
    }
}
