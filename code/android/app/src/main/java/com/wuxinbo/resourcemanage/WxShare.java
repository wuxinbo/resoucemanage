package com.wuxinbo.resourcemanage;

import android.content.Context;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxShare {


    public static IWXAPI api;
    void shareText(Context context){
        if(api==null){
           api = WXAPIFactory.createWXAPI(context,"");
           api.registerApp("");
        }
        WXTextObject object =new WXTextObject();
        object.text ="hello";
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = object;
        msg.description = object.text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "text";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        //调用api接口，发送数据到微信
        api.sendReq(req);

    }

}
