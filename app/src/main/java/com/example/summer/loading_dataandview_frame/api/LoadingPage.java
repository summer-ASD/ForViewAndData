package com.example.summer.loading_dataandview_frame.api;


import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.summer.loading_dataandview_frame.R;
import com.example.summer.loading_dataandview_frame.util.Utils;

import java.util.List;

/**
 * 管理界面加载的逻辑：
 * 定义界面加载状态，根据不同的状态去显示不同的界面
 */
public abstract class LoadingPage extends FrameLayout {

    public LoadingPage(Context context) {
        this(context,null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLoadingPage();
    }

    //1.定义界面的3种状态
    public enum PageState{
        STATE_LOADING,//加载中的状态
        STATE_SUCCESS,//加载成功的状态
        STATE_ERROR//加载失败的状态
    }
    private PageState mState = PageState.STATE_LOADING;//表示当前的状态,默认是加载中的状态

    private View loadingView;//加载中的View
    private View successView;//加载数据成功的View
    private View errorView;//加载失败的View

    /**
     * 初始化LoadingPage, 预先添加3种状态对应的View对象
     */
    private void initLoadingPage(){
        //1.添加loadingView
        if(loadingView==null){
            loadingView = View.inflate(getContext(),R.layout.page_loading ,null);
        }
        addView(loadingView);

        //2.添加errorView
        if(errorView==null){
            errorView = View.inflate(getContext(), R.layout.page_error ,null);
            Button btn_reload = (Button) errorView.findViewById(R.id.btn_reload);
            btn_reload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.先显示正在加载
                    mState = PageState.STATE_LOADING;
                    showPage();
                    //2.重新加载
                    loadData();
                }
            });
        }
        addView(errorView);

        //3.添加successView
        if(successView==null){
            successView = createSuccessView();//需要一个successView
        }
        if(successView!=null){
            addView(successView);
        }else {
            //如果successView是空，那么就没有必要往下执行，则抛出容易理解的异常
            throw new IllegalArgumentException("The successView can not be null!");
        }

        //4.当前界面默认是显示loadingView的
        showPage();

        //5.显示完默认界面，就去加载数据了
        loadData();
    }

    /**
     * 根据不同的状态去显示不同的View
     */
    private void showPage(){
        //1.先隐藏所有滴的View
        loadingView.setVisibility(View.INVISIBLE);
        successView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        //2.谁滴状态谁显示
        switch (mState){
            case STATE_LOADING:
                loadingView.setVisibility(View.VISIBLE);
                break;
            case STATE_SUCCESS:
                successView.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                errorView.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 加载数据，然后根据加载回来滴数据，更新界面
     */
    public void loadData(){
        //在子线程去加载数据
        new Thread(){
            @Override
            public void run() {
                super.run();
                //模拟请求服务器数据的延时时间
                SystemClock.sleep(1000);

                //1.接收每个界面请求数据方法的返回值
                Object data = onLoad();
                //2.根据数据判断当前界面的状态，
                mState = checkData(data);
                //3.根据最新的mState，刷新界面
                Utils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showPage();
                    }
                });
            }
        }.start();
    }

    /**
     * 根据返回的数据对象，判断其对应的界面状态
     * @param data
     * @return
     */
    protected PageState checkData(Object data){
        if(data==null){
            /**返回加载数据失败的状态**/
            return PageState.STATE_ERROR;
        }else {
            //判断list类型数据的size是否为0
            if(data instanceof List){
                List list = (List) data;
                if(list.size()==0){
                    //size为0，我们也设定为属于加载数据失败的情况
                    return PageState.STATE_ERROR;
                }
            }
            return PageState.STATE_SUCCESS;
        }
    }

    /**
     * 抽取接收每个界面请求的数据对象，具体怎么请求不用管，由子类去实现
     * 子类负责返回所请求的数据对象
     * @return
     */
    protected abstract Object onLoad();

    /**
     * 获取successView，每个界面的successView都不一样，需要由每个子类
     * 自己去实现
     * @return
     */
    protected abstract View createSuccessView();

}
