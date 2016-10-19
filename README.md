# GridViewPager
原文的博客：[Android 仿美团网，探索使用ViewPager+GridView实现左右滑动查看更多分类的功能](http://blog.csdn.net/qq_20785431/article/details/52528404) 

---
原先的布局代码是写在activity_main中的，我做的也就是将它作为library独立做成一个自定义控件，并优化了图片加载的逻辑而已。一点微小的工作。
实现的效果图与原来一模一样
###使用：
```
//使用builder模式设计初始化
mGridViewPager
    //设置每一页的容量
    .setPageSize(9)
    .setImageSetListener(new ImageSetListener() {
        @Override
        public void setImage(ImageView iv, int pos) {
            //图片加载方式任选
            iv.setBackgroundResource(mImgRes.get(pos));
        }
    })
    .setGridItemClickListener(new GridItemClickListener() {
        @Override
        public void click(int pos, int position, String str) {
            Log.d("123", pos + "/" + str);
        }
    })
    .setGridItemLongClickListener(new GridItemLongClickListener() {
        @Override
        public void click(int pos, int position, String str) {
            Log.d("456", pos + "/" + str);
        }
    })
    //传入String的List 必须作为最后一步
    .init(initData());
```

---
![image](https://github.com/hkq325800/GridViewPager/blob/master/20160913185125647.gif?raw=true)