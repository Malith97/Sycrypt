package com.synnlabz.sycryptr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class TypeAdapter extends ArrayAdapter<TypeItem> {
    public TypeAdapter(Context context, ArrayList<TypeItem> itemList) {
        super(context, 0, itemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dropdown_type, parent, false
            );
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textViewName = convertView.findViewById(R.id.text_view);

        TypeItem currentItem = getItem(position);

        if (currentItem != null) {
            imageView.setImageResource(currentItem.getFlagImage());
            textViewName.setText(currentItem.getTypeName());
        }

        return convertView;
    }
}
