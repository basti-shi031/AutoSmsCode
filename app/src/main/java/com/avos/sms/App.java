package com.avos.sms;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by lzw on 14-8-21.
 */
public class App extends Application {
  public void onCreate() {
    // 请用你的AppId，AppKey。并在管理台启用手机号码短信验证
    AVOSCloud.initialize(this, "JowqsI5zLKLSWCXPFnjqJWYP",
        "tCXQDp0XRbVEnNKE9FAvBCBl");
  }
}
