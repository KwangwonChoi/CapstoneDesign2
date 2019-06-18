package healthcare.ckwpjt.com.healthcare.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import healthcare.ckwpjt.com.healthcare.Services.AlertService;
import healthcare.ckwpjt.com.healthcare.R;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntent = new Intent(getApplicationContext(), AlertService.class);
        startService(mIntent);

        ImageButton logBtn = findViewById(R.id.main_log_btn);
        ImageButton inqBtn = findViewById(R.id.main_inq_btn);
        ImageButton iptvBtn = findViewById(R.id.main_iptv_btn);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogActivity.class);
                startActivity(intent);
            }
        });

        inqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InquireActivity.class);
                startActivity(intent);
            }
        });

        iptvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IPTVActivity.class);
                startActivity(intent);
            }
        });

    }
}
