package com.mykeygenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class CreatedKey extends AppCompatActivity {
    private Button save;
    private Button delete2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_key);

        TextView tv4=findViewById(R.id.textView4);
        String st=getIntent().getExtras().getString("textView4");
        tv4.setText(st);

        delete2=(Button) findViewById(R.id.delete2);
        delete2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                        openMain();

            }
        });

        Button save=(Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                RequestQueue rq;
                rq = Volley.newRequestQueue(CreatedKey.this);
                EditText et=findViewById(R.id.name);

                String saveURL="http://192.168.1.4:8085/KeyGenerator/rest/generate/save/"+getMacAddr().replace(":","")+"/"+et.getText()+"/"+getIntent().getExtras().getString("textView4");
                Log.i("url", "onClick: "+saveURL);
                JsonObjectRequest or=new JsonObjectRequest(Request.Method.GET, saveURL,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("success", "onResponse: successful request");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                }
                );

                rq.add(or);
                openMain();

            }
        });
    }

    private void openMain() {
        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);
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
