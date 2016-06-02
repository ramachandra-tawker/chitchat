package com.usimedia.chitchat;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Contacts extends AppCompatActivity {
    private static final String CONTACTS_SERVICE_URL = "http:192.168.1.7:8000/Contact";
    private  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    int numberOfContact;
    Set<String> contactNumbers;

    private List<String> getContacts(List<String> phoneNumbers) throws JSONException, IOException {
        //list to JSONArray to JSONObject
        JSONArray jsonNumbers = new JSONArray(phoneNumbers);
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("numbers", jsonNumbers);

        String rawResult = post(CONTACTS_SERVICE_URL, jsonRequest.toString());
        Log.d("Debug", rawResult);
        JSONObject jsonResult = new JSONObject(rawResult);

        JSONArray jsonContacts = jsonResult.getJSONArray("contacts");
        //pass to doinbg which accepts only list
        final List<String> contactList = new ArrayList<>();
        if(null != jsonContacts) {

            for (int i=0; i<jsonContacts.length(); i++){
                contactList.add(jsonContacts.getJSONObject(i).getString("username"));
            }
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

    private class ViewContacts extends AsyncTask<String, Void, List>
    {
       protected List<String> doInBackground(String...nos){//it takes a param of type String from oncreate
           List<String> result = new ArrayList<>(); // result given by getcontacts, sent to postexe
           try{
               List<String> phoneNumbers = Arrays.asList(nos);//to convert string array to list
               result = getContacts(phoneNumbers);// to contact db to check for the no
           }
           catch (JSONException e){
               e.printStackTrace();
           }catch (IOException e){
               e.printStackTrace();
           }
           return result;

       }

        protected void onPostExecute(List result){
            final ArrayAdapter<String> contactlistadapter = new ArrayAdapter<String>(
                    Contacts.this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    result
            );

            //arrayadapter to appear
            ListView contactlistview = (ListView) findViewById(R.id.listView_activity_contacts);
            //showing the adapter in the listview variable
            contactlistview.setAdapter(contactlistadapter);
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
