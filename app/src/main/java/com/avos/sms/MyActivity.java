package com.avos.sms;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;

public class MyActivity extends Activity implements View.OnClickListener {
  /**
   * Called when the activity is first created.
   */
  View send, verify;
  EditText phoneEdit, codeEdit;
  private Activity cxt;
  private AutoSmsBroadcastReceiver mAutoSmsBroadcastReceiver;
  private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    findView();
    cxt = this;

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
  }

  private void findView() {
    send = findViewById(R.id.sendCode);
    verify = findViewById(R.id.verify);
    phoneEdit = (EditText) findViewById(R.id.phoneEdit);
    codeEdit = (EditText) findViewById(R.id.codeEdit);
    send.setOnClickListener(this);
    verify.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.sendCode) {
      final String phone = phoneEdit.getText().toString();
      sendCode(phone);
    } else {
      final String code = codeEdit.getText().toString();
      verifyCode(code);
    }
  }

  private void verifyCode(String code) {
    AVOSCloud.verifySMSCodeInBackground(code, phoneEdit.getText().toString(),
        new AVMobilePhoneVerifyCallback() {
          @Override
          public void done(AVException e) {
            if (e == null) {
              toast(R.string.verifySucceed);
            } else {
              e.printStackTrace();
              toast(R.string.verifyFailed);
            }
          }
        });
  }

  public void sendCode(final String phone) {
    new AsyncTask<Void, Void, Void>() {
      boolean res;

      @Override
      protected Void doInBackground(Void... params) {
        try {
          AVOSCloud.requestSMSCode(phone, "SmsDemo", "注册", 10);
          res = true;
        } catch (AVException e) {
          e.printStackTrace();
          res = false;
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (res) {
          toast(R.string.sendSucceed);
        } else {
          toast(R.string.sendFailed);
        }
      }
    }.execute();
  }

  private void toast(int id) {
    Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.unregisterReceiver(mAutoSmsBroadcastReceiver);
  }
}
