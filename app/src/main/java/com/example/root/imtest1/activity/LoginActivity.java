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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.imtest1.R;
import com.example.root.imtest1.application.MyApplication;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    //用户名编辑框
    private AutoCompleteTextView mUserNameView;
    //密码编辑框
    private EditText mPasswordView;
    //进度条
    private View mProgressView;
    //登陆格式验证
    private View mLoginFormView;

    private Presence presence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            //EditorInfo.IME_NULL代表enter键被按下
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mUserCreateButton = (Button)findViewById(R.id.user_create);
        mUserCreateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserCreateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * 处理LUserCreate的返回信息
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            switch (resultCode) {
                case MyApplication.CREATE_USER_SUCCESS:
                    //TODO :

                    break;
                case MyApplication.CREATE_USER_FAILED:
                    //TODO
                    Log.e("创建程序失败","5");
                    finish();
                    break;
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     * --attemptLogin函数是登录验证的调用函数，
     * 按键和密码框的响应时间调用attemptLogin来做用户验证，
     * 他主要的功能是验证用户输入密码和邮箱的格式的正确与否，
     * 如果格式错误，在输入框中显示格式错误信息类型，格式正确后，
     * 调用showProgress显示用户验证延时等待对话框和启动mAuthTask异步处理用户信息验证。
     */
    private void attemptLogin() {

        // Reset errors.
        //设置输入框的错误提示为空
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user_name = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.login_error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user_name)) {
            mUserNameView.setError(getString(R.string.login_error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isEmailValid(user_name)) {
            mUserNameView.setError(getString(R.string.login_error_invalid_email));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // 如果格式错误，输入框重新获得输入焦点
            focusView.requestFocus();
        } else {
            // 如果输入的格式正确，显示验证等待对话框，并启动验证线程
            showProgress(true);
            if (mAuthTask != null) {
                //mAuthTask.execute((Void) null);
                return;
            } else {
                mAuthTask = new UserLoginTask(user_name, password);
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


    private int XMPPLogin(String mEmail, String mPassword) {

        try {
            // Simulate network access.
            Thread.sleep(1000);
            //初始化连接配置和属性
            MyApplication.setUpXmppTcpConnection();
            //登陆操作
            XMPPTCPConnection connection = MyApplication.getConnection();
            connection.connect();
            connection.login(mEmail, mPassword);
            //AccountManager manager = AccountManager.getInstance(MyApplication.appConnection);
            //String domin = XmppStringUtils.parseResource( MyApplication.appConnection.getUser());
            //Log.e("domin", domin);

            presence = new Presence(Presence.Type.available);
            presence.setStatus("I am online");
            MyApplication.appConnection.sendStanza(presence);

            /*Roster roster = Roster.getInstanceFor(MyApplication.appConnection);
            roster.setRosterLoadedAtLogin(true);*/
            return MyApplication.LOGIN_SUCCESS;

        } catch (InterruptedException e) {
            return MyApplication.THREAD_ERROR;
        } catch (XMPPException e) {
            e.printStackTrace();
            return MyApplication.XMPP_ERROR;
        } catch (SmackException e) {
            e.printStackTrace();
            return MyApplication.SMACK_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return MyApplication.IO_ERROR;
        }

        //傻逼似的写法，搞死自己了
        /*finally {
            //TODO: 出现异常的处理
            MyApplication.XmppConnectionDisconnect();
            MyApplication.setConnectionUsable(false);
        }*/
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            //连接函数
            return XMPPLogin(mEmail, mPassword);
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;
            showProgress(false);
            switch (success) {
                case MyApplication.LOGIN_SUCCESS:
                    Log.e("登陆成功", "0");
                    //TODO：这里开启新的activity
                    Intent intent = new Intent(LoginActivity.this, AppActivity.class);

                    startActivity(intent);
                    finish();
                    break;
                case MyApplication.SMACK_ERROR:
                    Log.e("smack错误", "1");
                    break;
                case MyApplication.XMPP_ERROR:
                    Log.e("xmpp服务器异常", "1");
                    mPasswordView.setError(getString(R.string.login_error_incorrect_password));
                    mPasswordView.requestFocus();
                    break;
                case MyApplication.THREAD_ERROR:
                    Toast.makeText(LoginActivity.this, "线程异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

