package com.liy.today.views.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.laiyifen.lyfframework.R;
import com.liy.today.utils.ViewUtils;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class ImageAction extends Action<ImageView>  {



     /**
     * Action init
     * @param resId 图片资源ID
     */
    public ImageAction(Context context, int resId) {
        super(getImageView(context));
        setImage(resId);
    }

    /**
     * Action init
     * @param resId 图片资源ID
     */
    public ImageAction(Context context, int resId, String badgeNumber) {
        super(getImageView(context), badgeNumber);
        setImage(resId);
    }

    private static ImageView getImageView(Context context) {
        ImageView icon = new ImageView(context);
        icon.setBackgroundResource(R.drawable.btn_transparent_in_black);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        icon.setMaxWidth((int) ViewUtils.dp2Px(context, 60));
        icon.setMinimumWidth((int) ViewUtils.dp2Px(context, 42));
        int padding = (int) ViewUtils.px2Dp(context, 10);
        icon.setPadding(padding, 0, padding, 0);
        return icon;
    }

    /**
     * 设置图片
     * @param resId 图片资源ID
     */
    public void setImage(int resId) {
        getView().setImageResource(resId);
    }

    /**
     * 设置图片
     * @param imageDrawable drawable
     */
    public void setImage(Drawable imageDrawable) {
        getView().setImageDrawable(imageDrawable);
    }


}
