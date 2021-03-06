package com.usimedia.chitchat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.usimedia.chitchat.adapter.ChatContactListAdapter;
import com.usimedia.chitchat.model.ChatContacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Contacts extends AppCompatActivity {
    private static final String CONTACTS_SERVICE_URL = "http://192.168.2.174:8000/Contact";
    private  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();


    private List<ChatContacts> getContacts(List<String> phoneNumbers) throws JSONException, IOException {
        //list to JSONArray to JSONObject
        JSONArray jsonNumbers = new JSONArray(phoneNumbers);
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("numbers", jsonNumbers);

        String rawResult = post(CONTACTS_SERVICE_URL, jsonRequest.toString());
        Log.d("Debug", rawResult);
        JSONObject jsonResult = new JSONObject(rawResult);

        JSONArray jsonContacts = jsonResult.getJSONArray("contacts");
        //pass to doinbg which accepts only list
        final List<ChatContacts> contactList = new ArrayList<>();

        ChatContacts currentContact ;

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < jsonContacts.length(); i++) {
            currentContact =  new ChatContacts();
            currentContact.setUsername(jsonContacts.getJSONObject(i).getString("username"));
            currentContact.setStatus(jsonContacts.getJSONObject(i).getString("status"));
            Log.d("contact", "current contact is " + currentContact.getUsername());
            try {
                currentContact.setLastseen(dateFormat.parse(jsonContacts.getJSONObject(i).getString("lastseen")));

            } catch (ParseException e) {
                e.printStackTrace();
                currentContact.setLastseen(null);
                Log.d("Contacts", "Current contact name being parsed = " + currentContact);
            }

            contactList.add(currentContact);

        }
        return contactList;
    }



    private String post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();

        return response.body().string();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

       // ListView contactList = (ListView) findViewById(R.id.listView_activity_contacts);
        //to contact phone db
        ContentResolver cr = getContentResolver();
        Uri providerUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] elementsrequired = new String[]{ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};


        //limits
        Cursor cursor = cr.query(providerUri,elementsrequired,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC ");

       // List<String> contactNames = new ArrayList<>();
        Set<String> contactNumbers = new HashSet<>();

        String number;

        while(cursor.moveToNext()){
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactNumbers.add(number);
            if (number != null){
                number = number.replaceAll("//a+","");
            }
        }


        final List<String> contactNames = new ArrayList<>(contactNumbers);//passing to doinbackground which should be converted to string.
        String[] a = new String[contactNames.size()];//declare a array a of size contactNames.size
        ViewContacts vc = new ViewContacts();//
        vc.execute(contactNames.toArray(a));//convert the contacts names list to a array of string

      /*  ArrayAdapter<String> contactListAdapter = new ArrayAdapter<>(
                Contacts.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                contactNames);

        contactList.setAdapter(contactListAdapter);*/

    }

    private class ViewContacts extends AsyncTask<String, Void, List<ChatContacts>>
    {
       protected List<ChatContacts> doInBackground(String...nos){//it takes a param of type String from oncreate


           List<ChatContacts> result = new ArrayList<>(); // result given by getcontacts, sent to postexe
           try{
               List<String> phoneNumbers = Arrays.asList(nos);//to convert string array to list
               //result = getContacts(phoneNumbers);// to contact db to check for the no, call to getcontacts function.
               List<ChatContacts> contacts = getContacts(phoneNumbers);
               Log.d("contacts", "number of contacts found " +contacts.size());
               return contacts;
           }
           catch (JSONException e){
               e.printStackTrace();
               return Collections.emptyList();
           }catch (IOException e){
               e.printStackTrace();
               return Collections.emptyList();

           }
           //return result;

       }
        @Override
        protected void onPostExecute(final List<ChatContacts> result){
          /*  final ArrayAdapter<ChatContacts> contactlistadapter = new ArrayAdapter<ChatContacts>(
                    Contacts.this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    result
            );*/

            Log.d("contacts", "creating adapter for " + result.size() + " contact ");
            ChatContacts[] chatarray = new ChatContacts[result.size()];
            final ArrayAdapter<ChatContacts> contactlist = new ChatContactListAdapter(
                Contacts.this,
                result.toArray(chatarray)
            );


            //arrayadapter to appear
            ListView contactlistview = (ListView) findViewById(R.id.listView_activity_contacts);
            //showing the adapter in the listview variable
            contactlistview.setAdapter(contactlist);

            contactlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toContactProfileActivity = new Intent(Contacts.this,ContactProfile.class);
                    toContactProfileActivity.putExtra("chat_contact", result.get(position) );
                    startActivity(toContactProfileActivity);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
