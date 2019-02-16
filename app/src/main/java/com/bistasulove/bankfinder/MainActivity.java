package com.bistasulove.bankfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    Button cityButton;
    Button ifscButton;
    EditText ifscText;
    EditText banktext;
    EditText citytext;
    Button ifscFindButton;
    Button cityFindButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showcityform(View view) {
        cityFindButton= findViewById(R.id.find_by_city);
        ifscFindButton=findViewById(R.id.find_by_ifsc);
        citytext=findViewById(R.id.city_name);
        banktext=findViewById(R.id.bank_name);
        ifscText=findViewById(R.id.ifsc_code);
        cityButton=findViewById(R.id.city_button);
        ifscButton=findViewById(R.id.ifsc_button);

        ifscFindButton.setVisibility(View.GONE);
        ifscText.setVisibility(View.GONE);
        banktext.setVisibility(View.VISIBLE);
        citytext.setVisibility(View.VISIBLE);
        cityFindButton.setVisibility(View.VISIBLE);

        cityButton.setBackgroundColor(getResources().getColor(R.color.button_clicked));
        ifscButton.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
        cityFindButton.setBackgroundColor(getResources().getColor(R.color.button_unclicked));

    }

    public void showifscform(View view) {
        cityFindButton= findViewById(R.id.find_by_city);
        ifscFindButton=findViewById(R.id.find_by_ifsc);
        citytext=findViewById(R.id.city_name);
        banktext=findViewById(R.id.bank_name);
        ifscText=findViewById(R.id.ifsc_code);
        cityButton=findViewById(R.id.city_button);
        ifscButton=findViewById(R.id.ifsc_button);
        ifscFindButton.setVisibility(View.VISIBLE);
        ifscText.setVisibility(View.VISIBLE);
        banktext.setVisibility(View.GONE);
        citytext.setVisibility(View.GONE);
        cityFindButton.setVisibility(View.GONE);

        cityButton.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
        ifscButton.setBackgroundColor(getResources().getColor(R.color.button_clicked));
        ifscFindButton.setBackgroundColor(getResources().getColor(R.color.button_unclicked));
    }

    public void findbankbycity(View view) {
        citytext=findViewById(R.id.city_name);
        banktext=findViewById(R.id.bank_name);
        Intent i = new Intent(this, BankActivity.class);
        i.putExtra("bankName", banktext.getText().toString());
        i.putExtra("cityName", citytext.getText().toString());
        i.putExtra("isFindByCity", "true");
        startActivity(i);

    }

    public void findbankbyifsc(View view) {
        ifscText=findViewById(R.id.ifsc_code);
        Intent i = new Intent(this, BankActivity.class);
        i.putExtra("ifscCode", ifscText.getText().toString());
        i.putExtra("isFindByCity", "false");
        startActivity(i);
    }

}
