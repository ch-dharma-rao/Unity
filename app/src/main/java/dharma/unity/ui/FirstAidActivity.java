package dharma.unity.ui;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import dharma.unity.R;

public class FirstAidActivity extends AppCompatActivity {

    CardView card1, card2, card3, card4, card5;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        card1 = (CardView) findViewById(R.id.card1);
        {
            card1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_fa_fractures);
                }

            });
        }

        card2 = (CardView) findViewById(R.id.card2);
        {
            card2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_fa_cuts);
                }

            });
        }


        card3 = (CardView) findViewById(R.id.card3);
        {
            card3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_fa_burns);
                }

            });
        }


        card4 = (CardView) findViewById(R.id.card4);
        {
            card4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_fa_sb);
                }

            });
        }


        card5 = (CardView) findViewById(R.id.card5);
        {
            card5.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    k = 2;
                    setContentView(R.layout.surv_fa_punchers);
                }

            });
        }

    }

    @Override
    public void onBackPressed() {

        if (k == 1 || k == 2) {
            Intent backpressedIntent = new Intent();
            backpressedIntent.setClass(getApplicationContext(), FirstAidActivity.class);
            startActivity(backpressedIntent);
            k = 0;
            finish();
        } else {
            super.onBackPressed();
        }

    }
}
