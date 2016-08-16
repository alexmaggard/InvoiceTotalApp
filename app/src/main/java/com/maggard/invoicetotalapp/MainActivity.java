package com.maggard.invoicetotalapp;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
        implements OnEditorActionListener {

    //implement global variables of all text and editText boxes that will be changing
    private EditText subTotalEditText;
    private TextView discountPercentView;
    private TextView discountAmountView;
    private TextView totalView;

    private String subTotalString;

    //define shared Preference
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get reference to your widgets
        subTotalEditText = (EditText) findViewById(R.id.subTotalEditText);
        discountPercentView = (TextView) findViewById(R.id.discountPercentView);
        discountAmountView =(TextView) findViewById(R.id.discountAmountView);
        totalView = (TextView) findViewById(R.id.totalView);

        //we have 1 editable text so we only need one action listener
        subTotalEditText.setOnEditorActionListener(this);

        //create SharedPreference object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    //onEditorAction controls the keyboard, D-Pad, or softkey..
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {         //change int i to "int actionId"
        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }
        return false;
    }
    //calculate information and update screen

    private void calculateAndDisplay() {
        subTotalString = subTotalEditText.getText().toString(); //get subtotal that the user entered
        float subTotal; //set local variable for subTotal
        if(subTotalString.equals("")){  // you must check if the information is blank, if so put in a number
            subTotal = 0;
        }
        else{
            subTotal = Float.parseFloat(subTotalString); //if a number is entered take the text and parse to a float
        }

        //get discount percent

        float discountPercent = 0;
        if(subTotal >= 200){
            discountPercent = .2f;
        }
        else if(subTotal >= 100){
            discountPercent = .1f;
        }
        else{
            discountPercent=0;
        }

        //calculate discount
        float discountAmount = subTotal * discountPercent;
        float total = subTotal - discountAmount;

        //now set the information back to the widget (display data on layout)
        NumberFormat percent = NumberFormat.getPercentInstance();   //create local variable
        discountPercentView.setText(percent.format(discountPercent)); //formatting the text and putting back into the widget

        NumberFormat discount = NumberFormat.getCurrencyInstance(); //create local variable
        discountAmountView.setText(discount.format(discountAmount)); //format the variable to a currency format
        totalView.setText(discount.format(total)); //sends information back to the widget
    }

    @Override
    protected void onPause() {
        //save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("subTotalString", subTotalString);
        editor.commit();                         //submits the information
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //get instance variables from editor
        subTotalString = savedValues.getString("subTotalString","");

        //call calculate and display
        calculateAndDisplay();
    }
}
