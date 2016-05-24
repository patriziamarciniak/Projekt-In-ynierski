package com.example.hp.sqlite.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hp.firstapp2.R;

import com.example.hp.sqlite.model.Contacts;


public class ListContactsAdapter extends BaseAdapter {

    public static final String TAG = "ListContactsAdapter";

    private List<Contacts> mItems;
    private LayoutInflater mInflater;

    public ListContactsAdapter(Context context, List<Contacts> listContacts) {
        this.setItems(listContacts);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Contacts getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            v = mInflater.inflate(R.layout.list_item_contact, parent, false);
            holder = new ViewHolder();
            holder.txtPhoneNumber = (TextView) v.findViewById(R.id.txt_phone_number);
            holder.txtName = (TextView) v.findViewById(R.id.txt_name);
            holder.txtLastName = (TextView) v.findViewById(R.id.txt_last_name);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        Contacts currentItem = getItem(position);
        if(currentItem != null) {
            holder.txtPhoneNumber.setText(currentItem.getPhoneNumber());
            holder.txtName.setText(currentItem.getName());
            holder.txtLastName.setText(currentItem.getLastName());
        }

        return v;
    }

    public List<Contacts> getItems() {
        return mItems;
    }

    public void setItems(List<Contacts> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txtPhoneNumber;
        TextView txtName;
        TextView txtLastName;
    }

}
