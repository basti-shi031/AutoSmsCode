# Android app自动填充短信验证码

短信验证码使用LeanCloud，具体使用方法 [短信验证码服务](http://leancloud.cn/docs/android_guide.html#短信验证码服务)

## 权限
增加短信读写权限，INTERNET和ACCESS_NETWORK_STATE权限为LeanCloud短信验证码功能所需的权限
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.RECEIVE_SMS"></uses-permission>
    <uses-permission android:name="android.permission.READ_SMS"/>
}
```

## BroadcastReceiver
重写onReceiver函数
```java
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
```

回调接口
```java
  //回调接口
    public interface MessageListener {
        public void onReceived(String message);
    }
```

## MainActivity
注册广播
```java
     mAutoSmsBroadcastReceiver = new AutoSmsBroadcastReceiver();
    //实例化过滤器并设置要过滤的广播
    IntentFilter intentFilter = new IntentFilter(ACTION);
    intentFilter.setPriority(Integer.MAX_VALUE);
    //注册广播
    this.registerReceiver(mAutoSmsBroadcastReceiver, intentFilter);

    mAutoSmsBroadcastReceiver.setOnReceivedMessageListener(new AutoSmsBroadcastReceiver.MessageListener() {
      @Override
      public void onReceived(String message) {
        codeEdit.setText(message);
      }
    });
```
反注册广播以免内存泄露
```java
  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.unregisterReceiver(mAutoSmsBroadcastReceiver);
  }
```
