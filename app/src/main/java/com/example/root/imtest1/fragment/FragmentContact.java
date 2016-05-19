package com.example.root.imtest1.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.imtest1.R;
import com.example.root.imtest1.activity.ChatActivity;
import com.example.root.imtest1.adapter.ListContactAdapter;
import com.example.root.imtest1.application.MyApplication;
import com.example.root.imtest1.content.ContactItem;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentContact.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContact extends Fragment {

    private static final String LOG_TAG = "FragmentContact";

    private static String CONTACT_LIST = "Contact_List";

    //传往ChatActivity的一些内容
    public static final String CHAT_CONTACT_JID = "CHAT_JID";

    private OnFragmentInteractionListener mListener;
    private List<ContactItem> mList = null;
    private ListView mListView;
    private ListContactAdapter mAdapter;

    public FragmentContact() {
        // Required empty public constructor
        mList = null;
    }

    public FragmentContact(List<ContactItem> list) {
        mList = list;
    }

    public void setList(List<ContactItem> list) {
        this.mList = list;
    }

    public List<ContactItem> getList() {
        return mList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mList = savedInstanceState.getParcelableArrayList(CONTACT_LIST);
        }
    }

    //这里设置listview 的 adapter
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        mList = getRoster(MyApplication.appConnection);

        mListView = (ListView) v.findViewById(R.id.fragment_contact_list);
        mAdapter = new ListContactAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startChat(mList.get(position));
            }
        });

        return v;
    }

    //联系人界面的点击跳转聊天界面的处理
    private boolean startChat(ContactItem item) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(CHAT_CONTACT_JID, item.getJid());
        startActivity(intent);
        return true;
    }

    //获取Roster
    private List<ContactItem> getRoster(XMPPTCPConnection connection) {
        List<ContactItem> rosterList = new ArrayList<>();

        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterGroup> groups = roster.getGroups();


        for(RosterGroup group: groups) {
            Collection<RosterEntry> entries = group.getEntries();
            Log.e(LOG_TAG, group.getName());
            for(RosterEntry entry: entries) {
                String name = entry.getName();
                String jid = entry.getUser();
                String groupname = group.getName();
                ContactItem item = new ContactItem(name, jid, 1, groupname);
                rosterList.add(item);
            }
        }

        return rosterList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentContact.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentContact newInstance(List<ContactItem> list) {
        ArrayList<ContactItem> data = new ArrayList(list);
        FragmentContact fragment = new FragmentContact(list);
        Bundle args = new Bundle();
        args.putParcelableArrayList(CONTACT_LIST, data);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
