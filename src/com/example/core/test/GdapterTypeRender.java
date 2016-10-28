package com.example.core.test;

import android.view.View;
/**
 * 
 * @author tangkun
 *
 */
public interface GdapterTypeRender {

    /**
     * 返回getView缓存
     * @return
     */
    public View getConvertView();

    /**
     * 填充item中各个控件的事件，比如按钮点击事件等
     */
    public void setEvents();

    /**
     * 对指定position的item进行数据的适配
     * @param position
     */
    public void setDatas(int position, boolean isLoading);
    

}
