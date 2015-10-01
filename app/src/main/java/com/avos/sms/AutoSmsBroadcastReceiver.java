package com.avos.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bowen on 2015/10/1.
 */
public class AutoSmsBroadcastReceiver extends BroadcastReceiver {

    private static MessageListener mMessageListener;
    private final static String regex = "[0-9]*[0-9]";

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
             byte[] pdu = (byte[]) obj;
             SmsMessage sms = SmsMessage.createFromPdu(pdu);
             String message = sms.getMessageBody();
             Log.d("短信内容", "message：" + message);

            //通过正则表达式提取验证码，正则表达式可根据实际短信样式修改
             Pattern p = Pattern.compile(regex);
             Matcher matcher  = p.matcher(message);
             while(matcher.find()){
                 mMessageListener.onReceived(matcher.group());
                 break;
             }
        }
    }

    //回调接口
    public interface MessageListener {
        public void onReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }

}
