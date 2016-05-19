package com.example.root.imtest1.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.imtest1.R;
import com.example.root.imtest1.content.ContactItem;

import java.util.List;
import java.util.Random;

/**
 * Created by root on 16-4-28.
 */
public class ListContactAdapter extends BaseAdapter {

    private static String TAG = "ListContactAdapter";

    private List<ContactItem> mList;
    private LayoutInflater mInflater;

    //用于头像颜色的Drawable
    private static Drawable contactHead;
    //头像颜色
    private static int[] COLOR;

    public ListContactAdapter(List<ContactItem> list, Context context) {
        mList = list;
        mInflater = LayoutInflater.from(context);

        contactHead = context.getResources().getDrawable(R.drawable.contact_image_background);

        COLOR = new int[5];
        initColor(context);

    }

    private void initColor(Context context) {
        COLOR[0] = context.getResources().getColor(R.color.contact_color_1);
        COLOR[1] = context.getResources().getColor(R.color.contact_color_2);
        COLOR[2] = context.getResources().getColor(R.color.contact_color_3);
        COLOR[3] = context.getResources().getColor(R.color.contact_color_4);
        COLOR[4] = context.getResources().getColor(R.color.contact_color_5);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        ContactItem contactItem = mList.get(position);
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_contact_item, null);
            viewHolder.image = (TextView) convertView.findViewById(R.id.fragment_contact_list_item_image);
            viewHolder.contactName = (TextView) convertView.findViewById(R.id.fragment_contact_list_item_contactname);
            viewHolder.contactJid = (TextView) convertView.findViewById(R.id.fragment_contact_list_item_jid);
            viewHolder.groupname = (TextView) convertView.findViewById(R.id.fragment_contact_list_item_groupname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setContactInfo(viewHolder, contactItem);
        return convertView;
    }

    private void setContactInfo(ViewHolder viewHolder, ContactItem item) {
        if(item != null) {
            setContactImage(viewHolder.image, item.getName());

            viewHolder.contactJid.setText(item.getJid());
            viewHolder.contactName.setText(item.getName());
            viewHolder.groupname.setText(item.getGroupName());
        } else {
            Log.e(TAG, "没有找到对应的Item");
        }

    }

    private void setContactImage(TextView head, String name) {
        Random random = new Random();
        int i = random.nextInt(5);
        contactHead.setTint(COLOR[i]);
        char c = name.charAt(0);

        head.setBackground(contactHead);
        head.setText(String.valueOf(c).toUpperCase());

    }

    class ViewHolder{
        public TextView image;
        public TextView contactName;
        public TextView contactJid;
        public TextView groupname;
    }
}
