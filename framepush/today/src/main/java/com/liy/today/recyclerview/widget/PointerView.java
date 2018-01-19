package com.liy.today.recyclerview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.laiyifen.lyfframework.R;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class PointerView extends View {
    private int x;
    private int y;
    private Bitmap finalPointerBitmap;
    private Bitmap pointerBitmap;
    public PointerView(Context context) {
        super(context);
        init();
    }

    public PointerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pointerBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.arrow));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureWidth(heightMeasureSpec));
        x = getMeasuredWidth();
        y = getMeasuredHeight();
        finalPointerBitmap = Bitmap.createScaledBitmap(pointerBitmap, x, y, true);
    }
    private int measureWidth(int widMeasureSpec){
        int result = 0;
        int size = MeasureSpec.getSize(widMeasureSpec);
        int mode = MeasureSpec.getMode(widMeasureSpec);
        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else{
            result = pointerBitmap.getWidth();
            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(finalPointerBitmap,0,0,null);
    }
}
