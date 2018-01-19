package com.liy.today.views.action;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.laiyifen.lyfframework.R;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class ActionTitleBar extends ViewSwitcher {

    private static final int VIEW_SWITCHER_ID_TITLE = 0;
    private static final int VIEW_SWITCHER_ID_CUSTOM = 1;
    private LinearLayout mLeftMenus;
    private LinearLayout mRightMenus;
    public TextView mTitleTxt;
    private ImageView mTitleImg;
    private RelativeLayout mCustomTitleBar;
    private LinearLayout mAppendLayout;

    private static final int MAX_MENUS_COUNT = 3;

    /**
     * init
     * @param context context
     */
    public ActionTitleBar(Context context) {
        super(context);
    }

    /**
     * init
     * @param context context
     * @param attrs attrs
     */
    public ActionTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftMenus = (LinearLayout) findViewById(R.id.left_menus);
        mRightMenus = (LinearLayout) findViewById(R.id.right_menus);
        mTitleTxt = (TextView) findViewById(R.id.title_txt);
        mTitleImg = (ImageView) findViewById(R.id.title_img);
        mCustomTitleBar = (RelativeLayout) findViewById(R.id.custom_title_bar);
        mAppendLayout = (LinearLayout) findViewById(R.id.title_bar_append_layout);
    }

    /**
     * 设置标题图片
     * @param titleImg titleImg
     */
    private void setTitleDrawable(@NonNull Drawable titleImg) {
        mTitleTxt.setText("");
        mTitleImg.setImageDrawable(titleImg);
    }

    /**
     * 设置标题图片
     * @param resId resId
     */
    public void setTitleDrawable(int resId) {
        Drawable resDrawable = getResources().getDrawable(resId);
        if (resDrawable != null) {
            mTitleTxt.setText("");
            mTitleImg.setImageDrawable(resDrawable);
        }
    }
    /**
     * 设置标题文字
     * @param resId resId
     */
    public void setTitle(int resId) {
        mTitleImg.setImageDrawable(null);
        mTitleTxt.setText(getResources().getText(resId));
    }

    /**
     * setTitle
     * @param title title
     */
    public void setTitle(@NonNull CharSequence title) {
        mTitleImg.setImageDrawable(null);
        mTitleTxt.setText(title);
    }

    /**
     * 设置title和CompoundDrawable
     * @param title title
     * @param resId resId
     * @param rule rule {@link Action#LEFT_OF or @link Action#RIGHT_OF}
     */
    public void setTitleAndCompoundDrawable(@NonNull CharSequence title, int resId, int rule) {
        setTitle(title);
        Drawable drawable=getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (rule == Action.LEFT_OF) {
            mTitleTxt.setCompoundDrawables(drawable, null, null, null);
        } else {
            mTitleTxt.setCompoundDrawables(null, null, drawable, null);
        }
    }

    /**
     * 添加Action Menu
     * @param action action
     */
    public void addAction(@NonNull Action action) {
        (action.getHorizontalRule() == Action.LEFT_OF ? mLeftMenus : mRightMenus)
                .addView(action.getParentView(), new LinearLayout.LayoutParams(-2, -1));
        checkMenusCount(action.getHorizontalRule());
    }

    private boolean checkMenusCount(int rule) {
        int menusCount = (rule == Action.LEFT_OF ? mLeftMenus : mRightMenus).getChildCount();
        return menusCount > MAX_MENUS_COUNT;
    }

    /**
     * 删除右边Action
     * @param action action
     */
    public void removeRightAction(@NonNull Action action) {
        if (mRightMenus.getChildCount() > 0) {
            mRightMenus.removeView(action.getView());
        }
    }

    /**
     * 删除左边Action
     * @param action action
     */
    public void removeLeftAction(@NonNull Action action) {
        if (mLeftMenus.getChildCount() > 0) {
            mLeftMenus.removeView(action.getView());
        }
    }

    /**
     * 删除右边的所有Action
     */
    public void removeAllRightAction() {
        if (mRightMenus.getChildCount() > 0) {
            mRightMenus.removeAllViews();
        }
    }

    /**
     * 删除左边的所有Action
     */
    public void removeAllLeftAction() {
        if (mLeftMenus.getChildCount() > 0) {
            mLeftMenus.removeAllViews();
        }
    }

    /**
     * 设置左边或者右边的Action
     * @param action action
     */
    public void setActionBarAction(@NonNull Action action) {
        if (action.getHorizontalRule() == Action.LEFT_OF) {
            removeAllLeftAction();
        } else {
            removeAllRightAction();
        }
        addAction(action);
    }


    /**
     * 隐藏ActionBar
     */
    public void hide() {
        setVisibility(View.GONE);
    }

    /**
     * 显示ActionBar
     */
    public void show() {
        setVisibility(View.VISIBLE);
    }

    /**
     * 显示自定义内容
     */
    public void showCustom() {
        setDisplayedChild(VIEW_SWITCHER_ID_CUSTOM);
    }

    /**
     * 是否正在显示自定义内容
     * @return true if yes
     */
    public boolean isShowCustom() {
        return getDisplayedChild() == VIEW_SWITCHER_ID_CUSTOM;
    }

    /**
     * 是否正在显示默认内容
     * @return true if yes
     */
    public boolean isShowNormal() {
        return getDisplayedChild() == VIEW_SWITCHER_ID_TITLE;
    }

    /**
     * 显示默认内容
     */
    public void showNormal() {
        setDisplayedChild(VIEW_SWITCHER_ID_TITLE);
    }

    /**
     * 获取默认TitleBar
     * @return RelativeLayout
     */
    public RelativeLayout getDefaultActionBar() {
        return (RelativeLayout) findViewById(R.id.action_title_bar);
    }

    /**
     * 设置自定义组件
     * @param customBar customBar
     */
    public void setCustomActionBarView(View customBar) {
        mCustomTitleBar.removeAllViews();
        mCustomTitleBar.addView(customBar, new RelativeLayout.LayoutParams(-1, -1));
    }

    /**
     * 设置ActionBar的高度
     * @param height height
     */
    public void setActionBarHeight(int height) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        } else {
            params.height = height;
        }
        setLayoutParams(params);
    }

    /**
     * 添加组件到titleBar
     * @param resId resId
     */
    public void appendViewTop(int resId){
        if (resId > 0) {
            appendViewTop(View.inflate(getContext(), resId, null));
        }
    }

    /**
     * 添加View到titleBar
     * @param view view
     */
    public void appendViewTop(View view){
        appendViewTop(view, new MarginLayoutParams(-1, -2));
    }

    /**
     * 添加View到titleBar
     * @param view view
     * @param params params
     */
    public void appendViewTop(View view, MarginLayoutParams params){
        mAppendLayout.addView(view, params);
    }

    /**
     * 将所有的View从titlebar appndlayout 移除
     */
    public void removeAllViewTop() {
        if (mAppendLayout != null && mAppendLayout.getChildCount() > 0) {
            mAppendLayout.removeAllViews();
        }
    }

    /**
     * 将某一个View从titleBar appendLayout移除
     * @param view view
     */
    public void removeViewTop(View view) {
        if (mAppendLayout != null && mAppendLayout.getChildCount() > 0 && view != null) {
            mAppendLayout.removeView(view);
        }
    }

    /**
     * 获取titleBar 标题
     * @return TextView
     */
    public TextView getTitleTxtView() {
        return mTitleTxt;
    }

    /**
     * 获取titleBar 标题
     * @return ImageView
     */
    public ImageView getTitleImgeView() {
        return mTitleImg;
    }

    /**
     * 获取titleBar 标题
     * @return View
     */
    public View getTitleView() {
        if (TextUtils.isEmpty(mTitleTxt.getText())) {
            return getTitleImgeView();
        }
        return getTitleTxtView();
    }

}
