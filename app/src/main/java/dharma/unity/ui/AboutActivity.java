package dharma.unity.ui;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import dharma.unity.R;

public class AboutActivity extends AppCompatActivity {

    CardView card1, card2, card3, card4, card5;
    Intent i1;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        card1 = (CardView) findViewById(R.id.earthquake);
        {
            card1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 1;
                    setContentView(R.layout.about_earthquake);
                }

            });
        }


        card2 = (CardView) findViewById(R.id.flood);
        {
            card2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 1;
                    setContentView(R.layout.about_floods);
                }

            });
        }


        card3 = (CardView) findViewById(R.id.house_fire);
        {
            card3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 1;
                    setContentView(R.layout.about_house_fire);
                }

            });
        }


        card4 = (CardView) findViewById(R.id.landslide);
        {
            card4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 1;
                    setContentView(R.layout.about_landslide);
                }

            });
        }


        card5 = (CardView) findViewById(R.id.drought);
        {
            card5.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 1;
                    setContentView(R.layout.about_drought);
                }

            });
        }

    }

    @Override
    public void onBackPressed() {

        if (k == 1) {
            Intent backpressedIntent = new Intent();
            backpressedIntent.setClass(getApplicationContext(), AboutActivity.class);
            startActivity(backpressedIntent);
            k = 0;
            finish();
        } else {
            super.onBackPressed();
        }

    }
}
