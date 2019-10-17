package com.mykeygenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowKeys extends AppCompatActivity {
    private KeyListAdapter keyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_keys);

        final ListView listView = (ListView) findViewById(R.id.listview);
        final ArrayList<Key> listItems = new ArrayList<Key>();
        final String json_string=getIntent().getExtras().getString("json_data");
        try {
            JSONObject o = new JSONObject(json_string);
            JSONArray a = o.getJSONArray("keys");

            int count = 0;
            while (count < a.length()) {
                JSONObject ob = a.getJSONObject(count);
                Key k = new Key(ob.getString("id"), ob.getString("name"), ob.getString("key"));

                listItems.add(k);

                count++;
            }

            keyAdapter = new KeyListAdapter(this, R.layout.list_adapter_view, listItems);
            listView.setAdapter(keyAdapter);

        }catch (Exception e){

        }


       listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowKeys.this);
                builder.setCancelable(true);
                builder.setTitle("Title");
                builder.setMessage("Do you want to delete this key ?");
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                delete(getMacAddr().replace(":", ""), listItems, position, keyAdapter);


                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }});


    }

    private void delete(String replace, final ArrayList<Key> listItems, final int position, final KeyListAdapter keyAdapter) {
        //delete key from database
        String deleteURL="http://192.168.1.4:8085/KeyGenerator/rest/generate/delete/"+replace+"/"+listItems.get(position).getKey().replace("\"","");
        RequestQueue rq;
        rq = Volley.newRequestQueue(ShowKeys.this);
        StringRequest or=new StringRequest(Request.Method.GET, deleteURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //delete key from table
                listItems.remove(position);
                keyAdapter.notifyDataSetChanged();

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowKeys.this);

                builder.setTitle("Title");
                builder.setMessage("key deleted !!");
                builder.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });


                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowKeys.this);

                builder.setTitle("Title");
                builder.setMessage("Can not delete the key !!");
                builder.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }
        );

        rq.add(or);



    }


    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
}
