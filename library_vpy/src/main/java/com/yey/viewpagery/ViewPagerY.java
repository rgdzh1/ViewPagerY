package com.yey.viewpagery;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerY extends ViewGroup {
    private int height;
    private int widht;
    private int currentIndex = 0;
    private int mDuration = 200;
    private Scroller myScroll;
    private Context mContext;
    private onPageChangeListener onPageChangeListener;
    private float mDownX, mLastX;
    private float interceptLastX, interceptLastY;

    public ViewPagerY(Context context) {
        this(context, null);
    }

    public ViewPagerY(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerY(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        // Scroller设置的是一个匀速插值器
        myScroll = new Scroller(context, new LinearInterpolator());
        // 初始化ImageLoader
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置ViewPagerY尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        widht = getMeasuredWidth();
        int wMeasureSpec = MeasureSpec.makeMeasureSpec(widht, MeasureSpec.EXACTLY);
        int hMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        //使子View显示
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(wMeasureSpec, hMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            this.getChildAt(i).layout(i * widht, 0, i * widht + widht, height);
        }
    }

    /**
     * 页面切换
     * @param index
     */
    public void moveTo(int index) {
        int duration = 0;
        if (index < 0) {
            index = 0;
        } else if (index > getChildCount() - 1) {
            index = getChildCount() - 1;
        }
        int count = currentIndex - index;
        if (count != 0) {
            duration = mDuration * count;
        } else {
            duration = mDuration;
        }
        currentIndex = index;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelect(currentIndex);
        }
        //缓慢移动
        int distanceX = currentIndex * getWidth() - getScrollX();
        //给MyScroll 计算的类赋初始值
        myScroll.startScroll(getScrollX(), 0, distanceX, 0, Math.abs(duration));
        invalidate();
    }

    @Override
    public void computeScroll() {
        //如果为true说明移动还没结束
        if (myScroll.computeScrollOffset()) {
            //得到计算的位置，然后移动
            float currX = myScroll.getCurrX();
            scrollTo((int) currX, 0);
            invalidate();
        }
    }

    /**
     * 设置页面改变时候回调监听。
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(onPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 为ViewPagerY设置资源集合
     *
     * @param res
     */
    public void setRes(List<ResType> res) {
        for (ResType mResType : res) {
            if (mResType.getmType() == ResType.Type.IAMG) {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageResource((Integer) mResType.getRes());
                this.addView(imageView);
            }
            if (mResType.getmType() == ResType.Type.URL) {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ImageLoader.getInstance().displayImage((String) mResType.getRes(), imageView);
                this.addView(imageView);
            }
            if (mResType.getmType() == ResType.Type.LAYOUT) {
                View view = LayoutInflater.from(mContext).inflate((Integer) mResType.getRes(), null);
                this.addView(view);
            }
        }
    }

    /**
     * 页面切换时候，每个页面滚动的时长。
     * @param duration
     */
    public void setScrollDuration(int duration) {
        mDuration = duration;
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 默认交给ViewGroup拦截事件,ViewGroup一般是不会拦截事件.
        boolean interceptChildeEvent = super.onInterceptTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = mDownX = interceptLastX = event.getX();
                interceptLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取当前手指坐标
                float moveX = event.getX();
                float moveY = event.getY();
                // 移动后,计算出滑动后与上一个坐标点之间的距离.
                float slopX = moveX - interceptLastX;
                float slopY = moveY - interceptLastY;
                // 得到手指滑动距离绝对值
                float slopAbsX = Math.abs(slopX);
                float slopAbsY = Math.abs(slopY);
                if ((slopAbsX > 0 || slopAbsY > 0) && (slopAbsX - slopAbsY) >= 6) {
                    // 如果手指移动距离大于0,且横向移动距离减去纵向移动距离大于6像素
                    // 那么ViewPagerY就将该事件拦截, 不分发给它的子View使用,留给自己使用了.
                    // 这样会导致mFirstTouchTarget=null,之后子View就再也接收不到事件组的其他事件了.
                    interceptChildeEvent = true;
                }
                interceptLastX = moveX;
                interceptLastY = moveY;
                break;
        }
        return interceptChildeEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                float mMoveX = event.getX();
                float mDiffX = mMoveX - mLastX;
                mLastX = mMoveX;
                if (event.getPointerId(event.getActionIndex()) == 0 && event.getPointerCount() == 1) {// 这个条件可以控制只追踪屏幕中的一个手指的滑动.
                    int scrollX = getScrollX();
                    if (currentIndex == 0) {
                        // 第一页
                        if (mDiffX < 0) {
                            // 如果向左滑动
                            ViewPagerY.this.scrollBy((int) -mDiffX, 0);
                        } else {
                            // 处理先向左滑动,然后又向右滑动.
                            float mDiffMargin = scrollX - mDiffX;
                            if (mDiffMargin >= 0) {
                                // 内容左边距离控件左边的距离减去向右滑动距离,如果大于0,说明内容左边距离控件左边还有间隔距离,滑动距离取手指移动距离.
                                ViewPagerY.this.scrollBy((int) -mDiffX, 0);
                            } else {
                                // 内容左边距离控件左边的距离减去向右滑动距离,如果小于0,说明内容左边与控件左边需要重合,滑动距离取getScrollX().
                                ViewPagerY.this.scrollBy(-scrollX, 0);
                            }
                        }
                    }
                    if (currentIndex == getChildCount() - 1) {
                        // 最后一页
                        if (mDiffX > 0) {
                            // 如果向右滑动
                            ViewPagerY.this.scrollBy((int) -mDiffX, 0);
                        } else {
                            // 处理先向右滑动,然后又向左滑动.
                            // (((getChildCount() - 1) * widht) - scrollX): 表示内容右边距离控件右边的距离
                            float mDiffMargin = (((getChildCount() - 1) * widht) - scrollX) + mDiffX;
                            if (mDiffMargin >= 0) {
                                // 说明内容右边与控件右边还有距离,滑动距离取手指移动距离.
                                ViewPagerY.this.scrollBy((int) -mDiffX, 0);
                            } else {
                                // 说明内容右边与控件右边需要重合,滑动距离取内容右边与控件右边的距离.
                                ViewPagerY.this.scrollBy(-(((getChildCount() - 1) * widht) - scrollX), 0);
                            }
                        }
                    }
                    if (currentIndex != 0 && currentIndex != getChildCount() - 1) {
                        // scrollBy总是和移动的相反
                        ViewPagerY.this.scrollBy((int) -mDiffX, 0);
                    }
//                    Log.e("测试 scrollX ", scrollX + "");
                }
                break;
            case MotionEvent.ACTION_UP:
                float mUpX = event.getX();
                if (mUpX - mDownX > getWidth() / 2) {
                    // 移动到上一个
                    moveTo(currentIndex - 1);
                } else if (mUpX - mDownX < -getWidth() / 2) {
                    // 移动到下一个
                    moveTo(currentIndex + 1);
                } else {
                    // 移动到当前页面
                    moveTo(currentIndex);
                }
                break;
        }
        return true;
    }

    public interface onPageChangeListener {
        void onPageSelect(int position);
    }
}
