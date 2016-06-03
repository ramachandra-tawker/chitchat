package com.usimedia.chitchat.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.usimedia.chitchat.R;
import com.usimedia.chitchat.model.ChatContacts;

/**
 * Created by USI IT on 6/3/2016.
 */
public class ChatContactListAdapter extends ArrayAdapter<ChatContacts> {

    private static final int CHAT_CONTACT_LAYOUT_ID = R.layout.chatcontact_listitem;
    private ChatContacts[] contacts;
    private Activity context;

    public ChatContactListAdapter(Activity activity, ChatContacts[] objects) {
        super(activity, CHAT_CONTACT_LAYOUT_ID, objects);
        contacts = objects;
        context = activity;
    }


    @Override //map each field in chat contact to the action of view inside the layout
    public View getView(int position, View convertView, ViewGroup parent) {

        View chatcontactview;
        if (convertView == null) {
            chatcontactview = context.getLayoutInflater().inflate(CHAT_CONTACT_LAYOUT_ID, parent, false); //inflate view inside the parent
        }
        else{
            chatcontactview=convertView;
        }
        ChatContacts currentcontact = contacts[position];

        TextView nametextview = (TextView) chatcontactview.findViewById(R.id.chat_contactlist_name);
        nametextview.setText(currentcontact.getUsername());
        Log.d("chat contact","creating view for "+currentcontact.getUsername());
        TextView lastseentextview = (TextView) chatcontactview.findViewById(R.id.chat_contactlist_lastseen);
        lastseentextview.setText(currentcontact.getLastseen().toString()); //to string bcoz it is a date format.

        TextView statustextview = (TextView) chatcontactview.findViewById(R.id.chat_contactlist_status);
        statustextview.setText(currentcontact.getStatus());

        return chatcontactview;

    }


}
