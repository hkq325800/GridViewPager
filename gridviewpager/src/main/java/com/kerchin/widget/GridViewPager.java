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
    private boolean hasCustomOval = false;
    private LayoutInflater inflater;
    private Context mContext;
    private ViewPager mPager;
    private LinearLayout mLlDot;
    private List<Model> mData;

    private List<GridView> mPagerList;
    private GridItemClickListener gridItemClickListener;
    private GridItemLongClickListener gridItemLongClickListener;

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
        initView();
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public GridViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view, this);
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) view.findViewById(R.id.ll_dot);
    }

    /**
     * necessary 必须作为最后一步
     *
     * @param list
     * @return
     */
    public GridViewPager init(List<Model> list) {
        mData = list;
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mData.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<GridView>();

        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
            gridView.setAdapter(new GridViewAdapter(mContext, mData, i, pageSize));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    if (gridItemClickListener == null) return;
                    int position = pos + curIndex * pageSize;
                    gridItemClickListener.click(pos, position, mData.get(position).getName());
                }
            });
            //true if the callback consumed the long click, false otherwise
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                    if (gridItemLongClickListener == null) return false;
                    else {
                        int position = pos + curIndex * pageSize;
                        gridItemLongClickListener.click(pos, position, mData.get(position).getName());
                        return true;
                    }
                }
            });
        }
        //设置适配器
        mPager.setAdapter(new ViewPagerAdapter<GridView>(mPagerList));
        //设置圆点
        if (!hasCustomOval) setOvalLayout();
        return this;
    }

    /**
     * optional 设置自定义圆点
     */
    public void setOvalLayout(View view, ViewPager.OnPageChangeListener listener) {
        hasCustomOval = true;
        mLlDot.removeAllViews();
        mLlDot.addView(view);
        mPager.addOnPageChangeListener(listener);
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

    /**
     * optional 设置单元点击事件
     *
     * @param listener
     * @return
     */
    public GridViewPager setGridItemClickListener(GridItemClickListener listener) {
        gridItemClickListener = listener;
        return this;
    }

    /**
     * optional 设置单元长按事件
     *
     * @param listener
     * @return
     */
    public GridViewPager setGridItemLongClickListener(GridItemLongClickListener listener) {
        gridItemLongClickListener = listener;
        return this;
    }

    public List<GridView> getmPagerList() {
        return mPagerList;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public GridViewPager setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getCurIndex() {
        return curIndex;
    }
}
