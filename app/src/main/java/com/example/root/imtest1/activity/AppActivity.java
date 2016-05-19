package com.example.root.imtest1.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.imtest1.R;
import com.example.root.imtest1.application.MyApplication;
import com.example.root.imtest1.fragment.FragmentAccount;
import com.example.root.imtest1.fragment.FragmentContact;
import com.example.root.imtest1.fragment.FragmentMessage;
import com.example.root.imtest1.view.AvatarView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "AppActivity:";
    //toolbar 的控件
    private AvatarView toolbar_iamge;
    private TextView toolbar_text;

    //fragment header 的图像显示
    private ImageView headerMessage;
    private ImageView headerContact;
    private ImageView headerAccount;

    //floating Button 控件
    private FloatingActionButton fab_addContact;
    private FloatingActionButton fab_logout;

    //用于记录当前的Fragment对象，用于切换
    private Fragment currentFragment;

    //ViewPager
    private ViewPager fragmentPager;
    private FragmentPagerAdapter fragmentAdapter;
    //List
    private List<Fragment> listFragment;
    //当前页
    private int currentPage = 0;

    //XMPP
    private XMPPTCPConnection XmppConnection;

    //fragment的处理
    FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        initView();
        toolbar_iamge.setAvatar(R.drawable.appmain_toolbar);

        XmppConnection = MyApplication.appConnection;
        if(XmppConnection.isConnected() == false) {
            Log.e(TAG, "XMPPConnection获取失败");
        } else {

        }

    }

    private void initView() {
        //toolbar 的控件
        toolbar_text = (TextView) findViewById(R.id.appmain_toolbar_text);
        toolbar_iamge = (AvatarView) findViewById(R.id.app_main_toolbar_image);
        //fragment header 的控件
        headerMessage = (ImageView) findViewById(R.id.app_main_messages);
        headerContact = (ImageView) findViewById(R.id.app_main_contacts);
        headerAccount = (ImageView) findViewById(R.id.app_main_account);
        //floating Button 控件
        fab_addContact = (FloatingActionButton) findViewById(R.id.fab_contact_add);
        fab_logout = (FloatingActionButton) findViewById(R.id.fab_logout);

        initFragment();

    }


    private void initFragment() {
        //所有的Fragment都是在这里初始化的
        //这里要做特殊的处理
        listFragment = new ArrayList<>();
        Fragment contact = new FragmentContact();
        Fragment account = new FragmentAccount();
        Fragment message = new FragmentMessage();
        listFragment.add(message);
        listFragment.add(contact);
        listFragment.add(account);
        //ViewPager
        fragmentPager = (ViewPager) findViewById(R.id.appmain_container);
        fragmentAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return listFragment.get(position);
            }

            @Override
            public int getCount() {
                return listFragment.size();
            }
        };
        fragmentPager.setAdapter(fragmentAdapter);

        //Adapter的事件处理
        fragmentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                headerMessage.setImageResource(R.drawable.appmain_message1);
                headerContact.setImageResource(R.drawable.appmain_contact1);
                headerAccount.setImageResource(R.drawable.appmain_account1);
                switch (position) {
                    case 0:
                        headerMessage.setImageResource(R.drawable.appmain_message2);
                        //mFragmentManager.beginTransaction().replace(R.id.)
                        break;
                    case 1:
                        headerContact.setImageResource(R.drawable.appmain_contact2);

                        break;
                    case 2:
                        headerAccount.setImageResource(R.drawable.appmain_account2);

                        break;
                    default:
                        Log.e(TAG, "onPageSelected no such position.");

                        break;
                }
                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_contact_add:
                //TODO 联系人添加事件的处理

                break;
            case R.id.fab_logout:
                //TODO 登出的处理

                break;
        }
    }
}
