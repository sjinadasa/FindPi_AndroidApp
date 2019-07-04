package com.findpi;

import android.app.Activity;
import android.widget.Button;


class findPiNewThread implements Runnable {

    private Activity mainActivity;

    private int decPlace;
    private int precisionInt;
    private findPiToDecimalPlace processPi;

    public findPiNewThread(Button findBtn, Activity mainActivity, int decPlace, int precision){
        this.mainActivity = mainActivity;
        this.decPlace = decPlace;
        this.precisionInt = precision;
    }

    @Override
    public void run() {
            // Do Pi finding process and print output
            processPi = new findPiToDecimalPlace(decPlace, precisionInt, this.mainActivity);

            processPi.execute();
    }
}
