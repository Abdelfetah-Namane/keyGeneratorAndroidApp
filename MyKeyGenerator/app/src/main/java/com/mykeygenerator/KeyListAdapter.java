package com.mykeygenerator;

import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class KeyListAdapter extends ArrayAdapter<Key> {
    private LayoutInflater minflater;
    private ArrayList<Key> keys;
    private int mRessource;

    public KeyListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Key> objects) {
        super(context, resource, objects);
        this.keys=objects;
        minflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mRessource=resource;
    }

     public View getView(int position , View convertView , ViewGroup parent){
         convertView =minflater.inflate(mRessource,null);

         Key k=getItem(position);


         TextView tvName=(TextView) convertView.findViewById(R.id.key_name);
         TextView tvkey=(TextView) convertView.findViewById(R.id.key_description);
         tvName.setText(k.getName());
         tvkey.setText(k.getKey());

         return convertView;
     }
}
