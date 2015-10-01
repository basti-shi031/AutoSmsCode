# LeanCloud 中的短信验证示例

见文档 [短信验证码服务](http://leancloud.cn/docs/android_guide.html#短信验证码服务)

[LeanCloud 站点下载地址](http://download.leancloud.cn/demo/)

## 安装
导入 LeanCloud SDK，
同时注意 App.java
```java
public class App extends Application{
  public void onCreate() {
    // 请用你的 AppId，AppKey。并在管理台启用手机号码短信验证
    AVOSCloud.initialize(this, "",
        "");
  }
}
```

## 发送验证短信

```java
AVOSCloud.requestSMSCode(phone, "应用名称", "操作名称", 10);  // 10 分钟内有效
```

## 判别验证码

```java
 AVOSCloud.verifySMSCodeInBackground(code, phone, new AVMobilePhoneVerifyCallback() {
      @Override
      public void done(AVException e) {
        if(e==null){
          toast(R.string.verifySucceed);
        }else{
          e.printStackTrace();
          toast(R.string.verifyFailed);
        }
      }
    });
```
