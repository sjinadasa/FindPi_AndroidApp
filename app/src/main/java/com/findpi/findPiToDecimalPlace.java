package com.findpi;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class findPiToDecimalPlace extends AsyncTask<Void, Integer, String>{

    private ProgressBar progressBar;
    private Activity mainActivity;
    private TextView resultsDisp;
    private Button findBtn;
    private ProgressBar horizontalBar;

    private int decPlace;

    // for Chudnovsky Algorithm
    private BigDecimal K = new BigDecimal("6");
    private BigDecimal L = new BigDecimal("13591409");
    private BigDecimal X = new BigDecimal("1");
    private BigDecimal M = new BigDecimal("1");
    private BigDecimal S = new BigDecimal("13591409");

    private int k;

    public findPiToDecimalPlace(int decPlace, int precision, Activity mainActivity){
        this.mainActivity = mainActivity;

        this.decPlace = decPlace;
        this.k = precision;
        this.resultsDisp = (TextView)mainActivity.findViewById(R.id.resultsView);
        this.findBtn = (Button)mainActivity.findViewById(R.id.findBtn);

    }

    @Override
    protected void onPreExecute(){
        progressBar = (ProgressBar) this.mainActivity.findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        this.horizontalBar = (ProgressBar)mainActivity.findViewById(R.id.horizontalBar);
        horizontalBar.setProgress(0);

    }

    @Override
    protected String doInBackground(Void... voids) {
        int progress = 1;
        publishProgress(progress);

        for (int i = 1; i <= k; i++) {
            // L(k+1) = L(k) + 545,140,134
            this.L = this.L.add(BigDecimal.valueOf(545140134));
            //System.out.println("L: " + this.L);

            // X(k+1) = X(k) * (-262,537,412,640,768,000)
            this.X = this.X.multiply(new BigDecimal("-262537412640768000"));

            // Because I used BigDecimal, all this had to be done. No regrets?
            // This is to get (K(k)^3 - 16K(k))/(k+1)^3
            // This is K(k)^3
            BigDecimal K3 = new BigDecimal(this.K.pow(3).toString());
            //This is 16K(k)
            BigDecimal K16 = new BigDecimal(this.K.multiply(BigDecimal.valueOf(16)).toString());
            //This is (K(k)^3 - 16K(k))
            BigDecimal K3MinusK16 = K3.subtract(K16);
            // This is (k+1)^3
            double kPlus1To3 = Math.pow((i), 3);
            //Finally we get (K(k)^3 - 16K(k))/(k+1)^3
            BigDecimal finalKforM = K3MinusK16.divide(BigDecimal.valueOf(kPlus1To3), this.decPlace, RoundingMode.HALF_UP);

            // M(k+1) = M(k)(K(k)^3 - 16K(k))/(k+1)^3
            this.M = this.M.multiply(finalKforM);

            // New K is set up using K(k+1) = K(k) + 12
            this.K = this.K.add(BigDecimal.valueOf(12));

            //So now we add the whole iteration in the form;
            // M(k) * L(k) / X(k)
            //We need helpers to get this done too due to BigDecimal
            BigDecimal stepSOne = this.M.multiply(this.L);

            BigDecimal stepSTwo = stepSOne.divide(this.X, this.decPlace, RoundingMode.HALF_UP);

            // Now we can update S
            this.S = this.S.add(stepSTwo);

            progress = i * 100 / k;
            publishProgress(progress);
        }

        // Once the iterations are done, we can get a final S which has to be multiplied by a C
        double C = 426880 * Math.sqrt(10005);
        BigDecimal pi = BigDecimal.valueOf(C).divide(this.S, this.decPlace, RoundingMode.HALF_UP);

        // Get only the required decimal places
        try {
            String piSol = pi.toString().substring(0, (decPlace + 2));
            return piSol;
        } catch (StringIndexOutOfBoundsException e) {
            String piSol = "3";
            return piSol;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        this.progressBar.setProgress(progress[0]);
        horizontalBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        this.progressBar.setVisibility(View.INVISIBLE);
        horizontalBar.setVisibility(View.INVISIBLE);
        this.resultsDisp.setText(result);
        this.resultsDisp.invalidate();
        findBtn.setVisibility(View.VISIBLE);
    }



}
