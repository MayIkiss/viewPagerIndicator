package com.example.wangguanghong.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangguanghong on 2017/1/17.
 */

public class ViewPagerIndicator extends LinearLayout {
    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTriangleHeight;
    private static final float TRIANGLE_WIDTH=1/6f;
    private final int DIMENSION_TRIANGLE_WIDTH_MAX= (int) (getScreenWidth()/3*TRIANGLE_WIDTH);//三角形最大宽度
    private int mInitTranslationX;
    private int mTranslationX;
    private int mVisibleTabCount;
    private static final int DEFAULT_TAB_COUNT=4;
    private static final int NORMAL_COLOR=0x77FFFFFF;
    private static final int HEIGHT_LIGHT=0xFFFFFFFF;
    private int mScreenWidth;
    private List<String> mItemTitles;
    private ViewPager mViewPager;
    private onPageChangeListener mPageChangeListener;

    public interface onPageChangeListener{
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    public void setPageChangeListener(onPageChangeListener pageChangeListener) {
        this.mPageChangeListener = pageChangeListener;
    }

    public ViewPagerIndicator(Context context) {
        this(context,null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t=context.obtainStyledAttributes(attrs,R.styleable.ViewPagerIndicator);
        mVisibleTabCount=t.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,DEFAULT_TAB_COUNT);
        if(mVisibleTabCount<1){
            mVisibleTabCount=DEFAULT_TAB_COUNT;
        }
        t.recycle();
        initPaint();
    }

    private void initPaint() {
        mPaint=new Paint();
        mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(15));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth=(int)(w/mVisibleTabCount*TRIANGLE_WIDTH);
        mTriangleWidth=Math.min(mTriangleWidth,DIMENSION_TRIANGLE_WIDTH_MAX);
        mInitTranslationX=w/mVisibleTabCount/2-mTriangleWidth/2;
        initTriangle();
    }

    /**
     * 初始化自定义图形
     */
    private void initTriangle() {
        mTriangleHeight=mTriangleWidth/2;
        mPath=new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHeight);
        mPath.close();
    }

    /**
     * 加载完xml之后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mVisibleTabCount;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX+mTranslationX,getHeight()+2);
        canvas.drawPath(mPath,mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 指示器跟随手指进行滚动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int width=getWidth()/mVisibleTabCount;
        mTranslationX=(int) (width*(offset+position));
        if(position>=mVisibleTabCount-2&&offset>0&&getChildCount()>mVisibleTabCount&&position<getChildCount()-2){
            if(mVisibleTabCount>1){
                this.scrollTo((position-(mVisibleTabCount-2))*width+(int)(offset*width),0);
            }else{
                this.scrollTo((int)(position+offset)*width,0);
            }
        }
        /**
         * 把第一个item挤出去一部分的情况
         */
        if(position==0){
            this.scrollTo(0,0);
        }
        invalidate();
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public int getScreenWidth() {
        WindowManager windowManager= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 设置title
     * @param itemTitles
     */
    public void setTitles(List<String> itemTitles){
        if(itemTitles!=null&&itemTitles.size()>0){
            this.removeAllViews();
            mItemTitles=itemTitles;
            for(String title:mItemTitles){
                addView(generateTextView(title));
            }
            setItemClickEvent();
        }
    }

    /**
     * 动态设置可见item数量
     */
    public void setItemCount(int count){
        mVisibleTabCount=count;
    }

    /**
     * 创建textview
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView view=new TextView(getContext());
        LinearLayout.LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width=getScreenWidth()/mVisibleTabCount;
        view.setTextColor(Color.parseColor("#FFFFFFFF"));
        view.setText(title);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        view.setTextColor(NORMAL_COLOR);
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 设置viewpager滑动监听和添加外部滑动回调
     */
    public void setViewPager(ViewPager viewpager,int position){
        mViewPager=viewpager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position,positionOffset);
                if(mPageChangeListener!=null){
                    mPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                lightHightText(position);
                if(mPageChangeListener!=null){
                    mPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(mPageChangeListener!=null){
                    mPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(position);
        lightHightText(position);
    }

    /**
     * 重置tab文本颜色
     */
    private void resetTextColor(){
        for (int i = 0; i < getChildCount(); i++) {
            setTextColor(i,NORMAL_COLOR);
        }
    }

    /**
     * 高亮text文本颜色
     */
    private void lightHightText(int pos){
        resetTextColor();
        setTextColor(pos,HEIGHT_LIGHT);
    }

    /**
     * 设置文本颜色
     */
    private void setTextColor(int pos,int color){
        View view = getChildAt(pos);
        if(view instanceof TextView){
            ((TextView)view).setTextColor(color);
        }
    }

    private void setItemClickEvent(){
        int itemCount=getChildCount();
        for (int i = 0; i < itemCount; i++) {
            final int j=i;
            View view=getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
