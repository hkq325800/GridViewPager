package com.kerchin.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucmed on 2016/10/19.
 */

public class GridViewPager extends RelativeLayout {
    private LayoutInflater inflater;
    private Context mContext;
    private ViewPager mPager;
    private LinearLayout mLlDot;
    private List<String> mData;
    private GridItemClickListener gridItemClickListener;
    private GridItemLongClickListener gridItemLongClickListener;
    private ImageSetListener imageSetListener;
    /**
     * 总的页数 计算得出
     */
    private int pageCount;

    /**
     * 每一页显示的个数 可设置
     */
    private int pageSize = 10;

    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    public GridViewPager(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view, this);
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) view.findViewById(R.id.ll_dot);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view, this);
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) view.findViewById(R.id.ll_dot);
    }

    public GridViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view, this);
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) view.findViewById(R.id.ll_dot);
    }

    public GridViewPager setData(List<String> list) {
        mData = list;
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mData.size() * 1.0 / pageSize);
        List<View> mPagerList = new ArrayList<View>();

        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
            gridView.setAdapter(new GridViewAdapter(mContext, mData, i, pageSize, imageSetListener));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    int position = pos + curIndex * pageSize;
                    gridItemClickListener.click(pos, position, mData.get(position));
                }
            });
            //true if the callback consumed the long click, false otherwise
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                    if (gridItemLongClickListener == null) return false;
                    else {
                        int position = pos + curIndex * pageSize;
                        gridItemLongClickListener.click(pos, position, mData.get(position));
                        return true;
                    }
                }
            });
        }
        //设置适配器
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        setOvalLayout();
        return this;
    }

    /**
     * 设置圆点
     */
    private void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public GridViewPager setGridItemClickListener(GridItemClickListener listener) {
        gridItemClickListener = listener;
        return this;
    }

    public GridViewPager setGridItemLongClickListener(GridItemLongClickListener listener) {
        gridItemLongClickListener = listener;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public GridViewPager setImageSetListener(ImageSetListener imageSetListener) {
        this.imageSetListener = imageSetListener;
        return this;
    }
}
