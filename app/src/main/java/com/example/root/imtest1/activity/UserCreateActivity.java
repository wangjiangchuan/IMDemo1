package com.example.root.imtest1.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.root.imtest1.R;
import com.example.root.imtest1.application.MyApplication;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.util.XmppStringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UserCreateActivity extends AppCompatActivity {

    private static final String TAG = "UserCreateActivity";
    //activity_user_create
    private UserLoginTask mAuthTask = null;
    //用户名编辑框
    private AutoCompleteTextView mUserNameView;
    private EditText mUserNickName;
    //密码编辑框
    private EditText mPasswordViewOne;
    private EditText mPasswordViewTwo;
    //进度条
    private View mProgressView;
    //登陆格式验证
    private View mLoginFormView;
    private AccountManager mAccountManager;
    private String accountJid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);
        // Set up the login form.
        mLoginFormView = (View) findViewById(R.id.create_user_form);
        mProgressView = findViewById(R.id.create_user_progress);

        mUserNameView = (AutoCompleteTextView) findViewById(R.id.create_user_username);
        mUserNickName = (EditText) findViewById(R.id.create_user_nickname);

        mPasswordViewOne = (EditText) findViewById(R.id.create_user_password_one);
        mPasswordViewTwo = (EditText) findViewById(R.id.create_user_password_two);

        Button mSignInButton = (Button) findViewById(R.id.create_user_register_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegiste();
            }
        });

        Button mUserCreateButton = (Button) findViewById(R.id.create_user_exit_button);
        mUserCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(MyApplication.CREATE_USER_FAILED);
                finish();
            }
        });
    }

    private void attemptRegiste() {
        // Reset errors.
        //设置输入框的错误提示为空
        mUserNameView.setError(null);
        mPasswordViewOne.setError(null);
        mPasswordViewTwo.setError(null);

        // Store values at the time of the login attempt.
        String user_name = mUserNameView.getText().toString();
        String password_one = mPasswordViewOne.getText().toString();
        String password_two = mPasswordViewTwo.getText().toString();
        String nickname = mUserNickName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 密码检查
        if (TextUtils.isEmpty(password_one) || TextUtils.isEmpty(password_two)) {
            mPasswordViewOne.setError("密码为空");
            focusView = mPasswordViewOne;
            cancel = true;
        } else {
            if (!password_one.equals(password_two)) {
                mPasswordViewOne.setError("两次密码必须一致");
                focusView = mPasswordViewOne;
                cancel = true;
            } else {
                if( !isPasswordValid(password_one)) {
                    mPasswordViewOne.setError("密码格式不正确");
                    focusView = mPasswordViewOne;
                    cancel = true;
                }
            }
        }
        // 账号检查
        if (TextUtils.isEmpty(user_name)) {
            mUserNameView.setError(getString(R.string.login_error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isEmailValid(user_name)) {
            mUserNameView.setError(getString(R.string.login_error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }
        //昵称检查
        if (TextUtils.isEmpty(nickname)) {
            mUserNickName.setError("此项必填");
            focusView = mUserNickName;
            cancel = true;
        }

        if (cancel) {
            // 如果格式错误，输入框重新获得输入焦点
            focusView.requestFocus();
        } else {
            // 如果输入的格式正确，显示验证等待对话框，并启动验证线程
            showProgress(true);
            if (mAuthTask != null) {
                mAuthTask.execute((Void) null);
                return;
            } else {
                mAuthTask = new UserLoginTask(user_name, password_one, nickname);
                mAuthTask.execute((Void) null);
                return;
            }
        }
    }

    //判断用户名和密码的格式是否正确
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }



    private int XMPPUserCreate(String mEmail, String mPassword, String nickname) {

        /*所有可能出现的异常
        XMPPException.XMPPErrorException
        SmackException.NoResponseException
        SmackException.NotConnectedException
        SmackException
        IOException
        XMPPException*/
        try {
            // Simulate network access.
            Thread.sleep(1000);
            //初始化连接配置和属性
            MyApplication.setUpXmppTcpConnection();
            //登陆操作
            MyApplication.appConnection.connect();
            mAccountManager = AccountManager.getInstance(MyApplication.appConnection);
            Map<String, String> map = new HashMap<String, String>();

            ByteBuffer buffer = ByteBuffer.wrap(nickname.getBytes());
            String s=new String(nickname.getBytes(),StandardCharsets.UTF_8);
            String UTF8_nickname = StandardCharsets.UTF_8.encode(nickname).toString();
            map.put("name", s);
            Log.e(TAG, UTF8_nickname);
            mAccountManager.createAccount(mEmail, mPassword, map);
            MyApplication.appConnection.login(mEmail, mPassword);
            accountJid = XmppStringUtils.parseBareJid(MyApplication.appConnection.getUser());

            //保存用户信息
            VCardManager manager = VCardManager.getInstanceFor(MyApplication.appConnection);
            VCard card = manager.loadVCard(accountJid);
            //card.setNickName(nickname);
            //card.setAvatar();  //设置用户头像的,暂时不用
            manager.saveVCard(card);
            return MyApplication.CREATE_USER_SUCCESS;
        } catch (InterruptedException e) {
            Log.e(TAG,String.valueOf(MyApplication.THREAD_ERROR));
            e.printStackTrace();
            return MyApplication.THREAD_ERROR;
        } catch (XMPPException e) {
            Log.e(TAG,String.valueOf(MyApplication.XMPP_ERROR));
            e.printStackTrace();
            return MyApplication.XMPP_ERROR;
        } catch (SmackException e) {
            Log.e(TAG,String.valueOf(MyApplication.SMACK_ERROR));
            e.printStackTrace();
            return MyApplication.SMACK_ERROR;
        } catch (IOException e) {
            Log.e(TAG,String.valueOf(MyApplication.IO_ERROR));
            e.printStackTrace();
            return MyApplication.IO_ERROR;
        } finally {
            //TODO: 出现异常的处理
            MyApplication.XmppConnectionDisconnect();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;
        private final String mNickName;

        UserLoginTask(String email, String password, String nickname) {
            mEmail = email;
            mPassword = password;
            mNickName = nickname;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            //连接函数
            return XMPPUserCreate(mEmail, mPassword, mNickName);
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;
            showProgress(false);
            switch (success) {
                case MyApplication.CREATE_USER_SUCCESS:
                    Log.e("创建用户成功", String.valueOf(MyApplication.CREATE_USER_SUCCESS));
                    //TODO
                    Intent intent = new Intent(UserCreateActivity.this, AppActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case MyApplication.IO_ERROR:
                case MyApplication.SMACK_ERROR:
                case MyApplication.THREAD_ERROR:
                case MyApplication.XMPP_ERROR:
                    Log.e("创建用户失败", String.valueOf(MyApplication.CREATE_USER_FAILED));
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        //获取运行平台的版本与应用的版本对比实现功能的兼容性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            ///获取系统定义的时间
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
