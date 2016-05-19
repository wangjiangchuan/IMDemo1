package com.example.root.imtest1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.imtest1.R;
import com.example.root.imtest1.content.MessageItem;

import java.util.List;

/**
 * Created by cuicui on 16/5/9.
 */
public class ListChatMsgAdapter extends BaseAdapter{

    private static final String LOG_TAG = "ListChatMsgAdapter";
    private List<MessageItem> list;

    private LayoutInflater inflater;

    public ListChatMsgAdapter(List<MessageItem> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        viewHolder = new ViewHolder();
        if(list.get(position).getPosition() == MessageItem.LEFT_CHAT_MSG) {
            convertView = inflater.inflate(R.layout.chat_item_left, null);
            viewHolder.image = (TextView) convertView.findViewById(R.id.chat_item_left_image);
            viewHolder.message = (TextView) convertView.findViewById(R.id.chat_item_left_text);
        } else {
            convertView = inflater.inflate(R.layout.chat_item_right, null);
            viewHolder.image = (TextView) convertView.findViewById(R.id.chat_item_right_image);
            viewHolder.message = (TextView) convertView.findViewById(R.id.chat_item_right_text);
        }

        initItem(viewHolder, list.get(position));
        return convertView;
    }

    private void initItem(ViewHolder view, MessageItem msg) {
        view.message.setText(msg.getContent());
        char name = msg.getUserName().charAt(0);
        view.image.setText(String.valueOf(name).toUpperCase());

    }

    class ViewHolder {
        public TextView message;
        public TextView image;
    }
}
