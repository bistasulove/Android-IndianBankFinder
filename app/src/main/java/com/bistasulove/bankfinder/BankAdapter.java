package com.bistasulove.bankfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class BankAdapter extends ArrayAdapter {

    public BankAdapter(Context context, List<Bank> banks) {
        super(context, 0, banks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if there is an existing list item view ( called ConvertView ) that we can reuse,
        //otherwise, if ConvertView is null, then inflate the new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.bank_list_item, parent, false);
        }

        //Find the Bank at the given position in the List of Bank's Details
        Bank currentBank = (Bank) getItem(position);

        //Find the TextView
        TextView bankdetailView = (TextView) listItemView.findViewById(R.id.bank_branch_details);

        //Display details in TextView
        bankdetailView.setText(currentBank.getBankDetails());

        return listItemView;
    }

}
