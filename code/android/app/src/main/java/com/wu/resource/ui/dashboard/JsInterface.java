package com.wu.resource.ui.dashboard;

import static com.wu.resource.Constant.PHOTO_GROUP_BY_SHOT_TIME;
import static com.wu.resource.Constant.URL;
import static com.wu.resource.dashboard.ChartData.LENS_KEY;
import static com.wu.resource.dashboard.ChartData.SHOT_TIME_KEY;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.room.util.StringUtil;

import com.wu.common.http.HttpUtil;
import com.wu.resource.ResourceApplication;
import com.wu.resource.dashboard.ChartData;
import com.wu.resource.db.ChartDao;

public class JsInterface {


    private WebView webView;
    private Context context;
    private ChartDao chartDao;
    public JsInterface(WebView webView,Context context) {
        this.webView = webView;
        this.context =context;
        ResourceApplication application = (ResourceApplication) ((Activity)context).getApplication();
        chartDao=application.getDb().chartDao();
    }

    /**
     * 根据拍摄时间统计数据
     */
    @JavascriptInterface
    public void loadShotTime(){
        queryChartData("loadShotTime",SHOT_TIME_KEY,"3");
    }
    private void queryChartData(String method,String key,String group){
        //初始化数据
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HttpUtil.getJson(URL+PHOTO_GROUP_BY_SHOT_TIME+"?group="+group,(res)->{
                //保存数据
                ChartData data =new ChartData();
                data.setKey(key);
                data.setValue(res);
                chartDao.insert(data);
                ((Activity)context).runOnUiThread(()->{
                    webView.evaluateJavascript("receiveData('"+method+"','"+res+"')",null);
                });
            });
        }
        ChartData data = chartDao.query(key);
        if(data!=null){
            ((Activity)context).runOnUiThread(()->{
                webView.evaluateJavascript("receiveData('"+method+"','"+ data.getValue() +"')",null);
            });
        }
    }
    /**
     * 镜头拍摄数据
     */
    @JavascriptInterface
    public void loadLens(){
        queryChartData("loadLens",LENS_KEY,"1");
    }
}
