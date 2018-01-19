package com.liy.today.views.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.laiyifen.lyfframework.R;
import com.liy.today.utils.ViewUtils;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class TextAction extends Action<TextView> {

    /**
     * Acton init
     * @param text 文字内容
     */
    public TextAction(Context context, String text) {
        super(getTextView(context));
        setText(text);
    }

    /**
     * Acton init
     * @param text 文字内容
     */
    public TextAction(Context context, String text, String badgeNumber) {
        super(getTextView(context), badgeNumber);
        setText(text);
    }

    /**
     * Action init
     * @param text 文字内容
     * @param resId 图片资源ID
     * @param rule 图片放置的方向
     */
    public TextAction(Context context, String text, int resId, int rule) {
        super(getTextView(context));
        setText(text);
        setTextDrawable(resId, rule);
    }

    /**
     * Action init
     * @param text 文字内容
     * @param resId 图片资源ID
     * @param rule 图片放置的方向
     */
    public TextAction(Context context, String text, int resId, int rule, String badgeNumber) {
        super(getTextView(context), badgeNumber);
        setText(text);
        setTextDrawable(resId, rule);
    }

    private static TextView getTextView(Context context) {
        return (TextView) View.inflate(context, R.layout.action_bar_text_view, null);
    }

    /**
     * 设置文字
     * @param textResourceId resource id
     */
    public void setText(int textResourceId) {
        getView().setText(textResourceId);
    }

    /**
     * 设置文字
     * @param text text
     */
    public void setText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            getView().setText(text);
        }
    }

    /**
     * 设置文字图片
     */
    public void setTextDrawable(int resId, int rule) {
        Drawable drawable = getContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (rule == LEFT_OF) {
            getView().setCompoundDrawables(drawable, null, null, null);
        } else if (rule == RIGHT_OF){
            getView().setCompoundDrawables(null, null, drawable, null);
        } else if (rule == TOP_OF) {
            getView().setPadding(0
                    , (int) ViewUtils.dp2Px(getContext(), 6)
                    , 0, (int) ViewUtils.dp2Px(getContext(), 6));
            getView().setCompoundDrawables(null, drawable, null, null);
            getView().setTextSize(12);
        } else if (rule  == BOTTOM_OF) {
            getView().setPadding(0
                    , (int) ViewUtils.dp2Px(getContext(), 6)
                    , 0, (int) ViewUtils.dp2Px(getContext(), 6));
            getView().setCompoundDrawables(null, null, null, drawable);
            getView().setTextSize(12);
        }
    }
}
