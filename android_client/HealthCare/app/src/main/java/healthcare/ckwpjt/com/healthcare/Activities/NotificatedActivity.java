
package healthcare.ckwpjt.com.healthcare.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import healthcare.ckwpjt.com.healthcare.Objects.ClientLog;
import healthcare.ckwpjt.com.healthcare.R;

public class NotificatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificated);

        try {
            ClientLog receivedValue = (ClientLog) getIntent().getExtras().getSerializable("receivedValue");
            TextView dateText = findViewById(R.id.noti_date_content);
            TextView stateText = findViewById(R.id.noti_state_content);
            TextView detailText = findViewById(R.id.noti_detail_content);

            dateText.setText(receivedValue.getDate());
            stateText.setText(receivedValue.getState());
            detailText.setText(receivedValue.getDetails());
        }
        catch (NullPointerException e){
            System.out.println(e.getStackTrace());
        }

        Button cancelBtn = findViewById(R.id.noti_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
