package com.example.administrator.keyboardadaptation;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2018\5\31 0031.
 */

public class KeyBoardUtils {

    private KeyBoardUtils() {
    }


    private static KeyBoardUtils keyBoardUtils = new KeyBoardUtils();

    public static KeyBoardUtils instance() {

        return keyBoardUtils;
    }

    /**
     * 键盘适配
     *
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     * scrollHeight滑动高度
     * <p>
     * 登录按钮的location坐标的y值，用来计算软键盘弹出后rootview向上滑动的高度
     */
    private int scrollHeight = 0;
    private int btnY = 0;

    //root最外成布局,scrollToView按钮布局
    protected void setKeyboardLayout(final View root, final View scrollToView, final Activity activity) {
        // 注册一个回调函数，当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
        root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        // 获取root在窗体的可视区域
                        root.getWindowVisibleDisplayFrame(rect);
                        // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
                        int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                        //   Log.i("tag", "最外层的高度" + root.getRootView().getHeight());
                        // Log.i("tag", "bottom的高度" + rect.bottom);
                        // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
                        if (rootInvisibleHeight > 100) {
                            //软键盘弹出来的时候
                            int[] location = new int[2];
                            // 获取scrollToView在窗体的坐标
                            scrollToView.getLocationInWindow(location);

                            //btnY的初始值为0，一旦赋过一次值就不再变化
                            if (btnY == 0) {
                                btnY = location[1];
                            }
                            if (scrollHeight == 0) {
                                // 计算root滚动高度，使scrollToView在可见区域的底部
                                scrollHeight = (btnY + scrollToView.getHeight()) - rect.bottom + 30;
                              //  Log.e("agt", "------scrollHeight----" + scrollHeight);
                                //平移属性动画(目标控件，方向，平移的起点位置和终点位置 的差，后面动画值可以多个)
                                ObjectAnimator.ofFloat(root, "translationY", scrollHeight, 0).setDuration(300).start();
                                root.scrollTo(0, scrollHeight);
                            }


                        } else {

                            // 软键盘没有弹出来的时候
                            if (scrollHeight > 0) {
                                // 平移属性动画(目标控件，方向，平移的起点位置和终点位置 的差，后面动画值可以多个)
                                ObjectAnimator.ofFloat(root, "translationY", -scrollHeight, 0).setDuration(300).start();
                                root.scrollTo(0, 0);
                                scrollHeight = 0;
                            } else {
                                root.scrollTo(0, 0);
                            }

                        }
                    }
                });


        //点击布局 关闭键盘
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(activity);

            }
        });
    }


    //关闭软键盘
    private void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
