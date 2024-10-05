package com.neal.android.upitplatforma;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
/**
 * Created by Loredana on 25.09.2018.
 */
public class Splash extends Activity {


    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final TextView txt = (TextView) findViewById(R.id.textView);
        txt.startAnimation(AnimationUtils.loadAnimation(Splash.this,R.anim.fade));

        Typeface myType = Typeface.createFromAsset(getAssets(),"Pacifico.ttf");
        txt.setTypeface(myType);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(Splash.this,MapsActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}