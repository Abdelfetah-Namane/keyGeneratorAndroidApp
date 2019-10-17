package com.mykeygenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.android.volley.Request.*;

public class MainActivity extends AppCompatActivity {
    private Button create;
    private final String CreateURL="http://192.168.1.4:8085/KeyGenerator/rest/generate";
    private String json_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //key generation button
        Button create=(Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                RequestQueue rq;
                rq = Volley.newRequestQueue(MainActivity.this);
                StringRequest or=new StringRequest(Request.Method.GET, CreateURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        openCreatedKey(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        openCreatedKey("an error has occurred "+error.getMessage());

                    }
                }
                );

                rq.add(or);


            }
        });


        //key table button
        Button show=(Button) findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                RequestQueue rq;
                rq = Volley.newRequestQueue(MainActivity.this);
                String ShowURL=CreateURL+"/show/"+getMacAddr().replace(":","");
                StringRequest or=new StringRequest(Request.Method.GET, ShowURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("eeeaaaaaaaaaaaaaaaaaaaaa","onResponse: "+response);

                        openShowKeys(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String message = error.getMessage();


                    }
                }
                );

                rq.add(or);


            }
        });

        //key generation button
        Button delete=(Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String deleteURL=CreateURL+"/show/"+getMacAddr().replace(":","");
                RequestQueue rq;
                rq = Volley.newRequestQueue(MainActivity.this);
                StringRequest or=new StringRequest(Request.Method.GET, deleteURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("deleeete","onResponse: "+response);
                        openDeleteKeys(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String message = error.getMessage();


                    }
                }
                );

                rq.add(or);


            }
        });
    }

    //this method is used to send a request to the backen in order to generate the code
    private void openCreatedKey(String key) {
        Intent intent = new Intent(this, CreatedKey.class);
        intent.putExtra("textView4", key);

        startActivity(intent);
    }

    //this method is used to get the mac adress of the android device
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

    //this method is used to send a request to the backend in order to get the list of the generated keys
    public void openShowKeys(String json_string){
        if(json_string==null) {
            Toast.makeText(getApplicationContext(), "cannot see the generated keys  ", Toast.LENGTH_LONG).show();
        }else{
            Intent intent=new Intent( this,ShowKeys.class);
            intent.putExtra("json_data",json_string);
            startActivity(intent);

        }

    }





}

