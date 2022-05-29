package dharma.unity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import dharma.unity.R;

public class SurvivalActivity extends AppCompatActivity {
    CardView card1, card2, card3, card4, card5, card6, card7;
    Intent i1;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival);


        card1 = (CardView) findViewById(R.id.card1);
        {
            i1 = new Intent(this, FirstAidActivity.class);
            card1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(i1);
                }
            });
        }
        ;

        card2 = (CardView) findViewById(R.id.card2);
        {

            card2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_food);

                }

            });
        }


        card3 = (CardView) findViewById(R.id.card3);
        {

            card3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_water);

                }

            });
        }


        card4 = (CardView) findViewById(R.id.card4);
        {

            card4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_earthquake);

                }

            });
        }


        card5 = (CardView) findViewById(R.id.card5);
        {

            card5.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_floods);

                }

            });
        }


        card6 = (CardView) findViewById(R.id.card6);
        {

            card6.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_house_fire);

                }

            });
        }


        card7 = (CardView) findViewById(R.id.card7);
        {

            card7.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_landslide);

                }

            });
        }


    }

    @Override
    public void onBackPressed() {

        if (k == 1 || k == 2) {

            Intent backpressedIntent = new Intent();
            backpressedIntent.setClass(getApplicationContext(), SurvivalActivity.class);
            startActivity(backpressedIntent);
            k = 0;
            finish();
        } else {
            super.onBackPressed();
        }

    }
}
