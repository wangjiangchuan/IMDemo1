package com.example.root.imtest1.application;

import android.app.Application;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * Created by root on 16-4-17.
 */
public class MyApplication extends Application {
    //XMPPConnection
    private static XMPPTCPConnectionConfiguration.Builder appConfigurationBuilder;
    public static XMPPTCPConnection appConnection = null;

    private static boolean isConnectionSetUp = false;


    //连接的基本配置
    public static final String HOST = "192.168.4.2";
    public static final int PORT = 5222;
    public static final String SERVICE_NAME = HOST;

    //创建连接时候出现的情况
    public static final int LOGIN_SUCCESS = 0;     //登陆成功
    public static final int SMACK_ERROR = 1;     //smack异常
    public static final int XMPP_ERROR = 2;     //xmpp服务器异常
    public static final int IO_ERROR = 3;       //IO异常
    public static final int THREAD_ERROR = 4;   //线程错误

    //创建新用户的activity返回值
    public static final int CREATE_USER_SUCCESS = 10;
    public static final int CREATE_USER_FAILED = 11;

    //应用主界面,登陆成功或者创建用户成功后的界面

    //XMPP初始化函数
    public static void setUpXmppTcpConnection() {

            appConfigurationBuilder = XMPPTCPConnectionConfiguration.builder();
            appConfigurationBuilder.setHost(HOST);
            appConfigurationBuilder.setPort(PORT);
            appConfigurationBuilder.setDebuggerEnabled(true);
            appConfigurationBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            appConfigurationBuilder.setServiceName(HOST);
    }

    public static void XmppConnectionDisconnect() {
        if(appConnection != null) {
            appConnection.disconnect();
            appConnection = null;
        }
        isConnectionSetUp = false;
    }

    public static XMPPTCPConnection getConnection() {

        if(isConnectionSetUp == true) {
            if(appConnection.isConnected())
                return appConnection;
            else {
                return newConnection();
            }
        } else {
            isConnectionSetUp = true;
            return newConnection();
        }
    }

    private static XMPPTCPConnection newConnection() {
        setUpXmppTcpConnection();
        appConnection = new XMPPTCPConnection(appConfigurationBuilder.build());
        return appConnection;
    }

}
