package com.findpi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button findBtn;
    private Button resetBtn;
    private EditText decInputField;
    private EditText precInputField;

    private TextView resultsDisp;
    private ProgressBar progressBar;
    private ProgressBar horizontalBar;
    public Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findBtn = (Button) findViewById(R.id.findBtn);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        decInputField = (EditText) findViewById(R.id.decPlacesInput);
        precInputField = (EditText) findViewById(R.id.precInput);
        resultsDisp = (TextView) findViewById(R.id.resultsView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        horizontalBar = (ProgressBar)findViewById(R.id.horizontalBar);

        mainActivity = this;

        // Setting progress bar invisible
        progressBar.setVisibility(View.INVISIBLE);
        horizontalBar.setVisibility(View.INVISIBLE);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsDisp.setText("");
                try{
                    hideSoftKeyboard();
                }catch (NullPointerException e){
                }

                try {
                    int decPlace = Integer.parseInt(decInputField.getText().toString());
                    if (decPlace <= 0) {
                        throw new IllegalArgumentException();
                    }
                    int precisionInt = Integer.parseInt(precInputField.getText().toString());
                    if (precisionInt <= 0 || precisionInt > 1000) {
                        throw new IllegalArgumentException();
                    }

                    // Finding Pi, making progress bar visible and find button invisible;
                    Thread findPi = new Thread(new findPiNewThread(findBtn, mainActivity, decPlace, precisionInt));
                    progressBar.setVisibility(View.VISIBLE);
                    horizontalBar.setVisibility(View.VISIBLE);
                    findBtn.setVisibility(View.INVISIBLE);
                    findPi.start();
                    resultsDisp.invalidate();

                    // allowing results to be selectable
                    resultsDisp.setTextIsSelectable(true);

                    // making reset button visible again
                    resetBtn.setVisibility(View.VISIBLE);


                } catch (IllegalArgumentException ie) {
                    resultsDisp.setText("Please check your inputs.");
                    resetBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                decInputField.setText("");
                precInputField.setText("");
                resultsDisp.setText("");
                resetBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
