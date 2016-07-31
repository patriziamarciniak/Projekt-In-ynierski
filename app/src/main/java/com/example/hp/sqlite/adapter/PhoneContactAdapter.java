package com.example.hp.sqlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.firstapp2.R;
import com.example.hp.sqlite.model.PhoneContact;

import java.util.List;

public class PhoneContactAdapter extends BaseAdapter {

    public static final String TAG = "PhoneContactAdapter";

    private List<PhoneContact> mItems;
    private LayoutInflater mInflater;
    private int layoutType;

    public PhoneContactAdapter(Context context, List<PhoneContact> listContact, int type) {
        this.setItems(listContact);
        this.mInflater = LayoutInflater.from(context);
        this.layoutType = type;
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0;
    }

    @Override
    public PhoneContact getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (layoutType == 0) {
            if (v == null) {
                v = mInflater.inflate(R.layout.list_item_contact, parent, false);
                holder = new ViewHolder();
                holder.txtName = (TextView) v.findViewById(R.id.txt_name);
                holder.txtPhoneNumber = (TextView) v.findViewById(R.id.txt_phone_number);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
        } else {
            if (v == null) {
                v = mInflater.inflate(R.layout.list_item_choosed_contact, parent, false);
                holder = new ViewHolder();
                holder.txtName = (TextView) v.findViewById(R.id.txt_name);
                holder.txtPhoneNumber = (TextView) v.findViewById(R.id.txt_phone_number);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            ImageView imageView = (ImageView) v.findViewById(R.id.iconDelete);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    final int position2 = position;
                    mItems.remove(position2); // remove the item from the data list
                    notifyDataSetChanged();
                }
            });
        }

        PhoneContact currentItem = getItem(position);
        if (currentItem != null) {
            holder.txtName.setText(currentItem.getName());
            holder.txtPhoneNumber.setText(currentItem.getPhoneNumber());
        }


        return v;
    }

    public List<PhoneContact> getItems() {
        return mItems;
    }

    public void setItems(List<PhoneContact> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txtName;
        TextView txtPhoneNumber;
    }

}
