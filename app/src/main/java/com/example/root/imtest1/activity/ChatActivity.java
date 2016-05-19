package com.example.root.imtest1.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.imtest1.R;
import com.example.root.imtest1.adapter.ListChatMsgAdapter;
import com.example.root.imtest1.application.MyApplication;
import com.example.root.imtest1.content.MessageItem;
import com.example.root.imtest1.fragment.FragmentContact;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.util.XmppStringUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "ChatActivity";
    //view
    private TextView chatName;
    private EditText chatMessage;
    private Button sendBut;
    private ImageButton backBut;

    //显示聊天内容的list
    private List<MessageItem> contentList;
    private ListView listview;
    private ListChatMsgAdapter listAdapter;

    //聊天对象的信息
    private String JIDChatWith;
    private String NameChatWith;
    private String thisName;

    //xmpp相关的
    private ChatManager chatManager;
    private ChatMessageListener messageListener;
    private ChatManagerListener managerListener;
    private Chat mChat;

    //首发消息的处理标志
    private static final int MSG_RECV = 1;
    private static final int MSG_SEND = 2;

    private static final String RECV_MSG_BODY = "msg_body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initXMPPChat();

    }

    private void initXMPPChat() {
        JIDChatWith = getIntent().getStringExtra(FragmentContact.CHAT_CONTACT_JID);
        thisName = XmppStringUtils.parseLocalpart(MyApplication.appConnection.getUser());
        NameChatWith = XmppStringUtils.parseLocalpart(JIDChatWith);

        chatManager = ChatManager.getInstanceFor(MyApplication.appConnection);
        managerListener = new MyChatManagerListener();
        chatManager.addChatListener(managerListener);
        mChat = chatManager.createChat(JIDChatWith);

        chatName.setText(NameChatWith);
    }

    private void initView() {
        chatName = (TextView) findViewById(R.id.chat_title);
        chatMessage = (EditText) findViewById(R.id.chat_message);
        sendBut = (Button) findViewById(R.id.chat_send_but);
        backBut = (ImageButton) findViewById(R.id.chat_back);

        sendBut.setOnClickListener(this);

        //初始化各种控件的显示
        chatName.setText("聊天用户的名字");

        initContentList();
    }

    private void initContentList() {
        listview = (ListView) findViewById(R.id.chat_content_list);
        contentList = new ArrayList<>();

        listAdapter = new ListChatMsgAdapter(contentList, this);
        listview.setAdapter(listAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_back:
                //TODO 点击返回键要处理的内容

                break;
            case R.id.chat_send_but:
                listview.scrollTo(chatMessage.getTop(), chatMessage.getLeft());
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        String message = chatMessage.getText().toString();
        if(message.isEmpty()) {
            Toast.makeText(this, "发送的信息为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO xmpp发送消息，发送成功才能添加到list中
        XMPPTCPConnection connection = MyApplication.appConnection;
        if(!connection.isConnected()) {
            connection = MyApplication.getConnection();
        }

        try {
            mChat.sendMessage(message);
            String name = XmppStringUtils.parseLocalpart(connection.getUser());
            addToList(message, name, MessageItem.RIGHT_CHAT_MEG);
        } catch (SmackException.NotConnectedException e) {
            Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
        }


        //消息发送成功后才能添加到list中
        //addToList(message, "用户1", );
    }


    class MyChatManagerListener implements ChatManagerListener{

        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            chat.addMessageListener(new MyChatMessageListener());
        }
    }

    class MyChatMessageListener implements ChatMessageListener {

        @Override
        public void processMessage(Chat chat, Message message) {
            String body = message.getBody();
            if(body == null) {
                return;
            } else {
                String user = XmppStringUtils.parseLocalpart(message.getFrom());
                MessageItem item = new MessageItem(user, body, MessageItem.LEFT_CHAT_MSG);
                android.os.Message msg = new android.os.Message();
                msg.what = MSG_RECV;
                Bundle bd = new Bundle();
                bd.putParcelable(RECV_MSG_BODY, item);
                msg.setData(bd);
                mHandler.sendMessage(msg);
            }
        }
    }

    private void addToList(String message, String user, int position) {

        MessageItem item = new MessageItem(user, message, position);
        contentList.add(item);
        listAdapter.notifyDataSetChanged();
        listview.setSelection(contentList.size() - 1);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RECV:
                    MessageItem item = (MessageItem) msg.getData().getParcelable(RECV_MSG_BODY);
                    addToList(item.getContent(), item.getUserName(), item.getPosition());
                    break;
                case MSG_SEND:

                    break;
            }
        }
    };

}


