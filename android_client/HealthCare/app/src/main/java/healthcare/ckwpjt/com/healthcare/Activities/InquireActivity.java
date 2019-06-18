package healthcare.ckwpjt.com.healthcare.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import healthcare.ckwpjt.com.healthcare.Objects.InquireMessage;
import healthcare.ckwpjt.com.healthcare.R;

public class InquireActivity extends AppCompatActivity {

    private final String mClientId = "KwangwonChoi/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);



        final EditText title = findViewById(R.id.inq_title_input);
        final EditText contents = findViewById(R.id.inq_contents_input);

        Button sendBtn = findViewById(R.id.inq_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference myRef = FirebaseDatabase.getInstance()
                        .getReference("Doctor/Messages/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                myRef.child("id").setValue(mClientId);
                myRef.child("title").setValue(title.getText().toString());
                myRef.child("contents").setValue(contents.getText().toString());

                finish();
            }
        });

        Button cancelBtn = findViewById(R.id.inq_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
