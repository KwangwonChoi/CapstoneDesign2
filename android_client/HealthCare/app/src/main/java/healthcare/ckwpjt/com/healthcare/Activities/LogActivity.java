package healthcare.ckwpjt.com.healthcare.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import healthcare.ckwpjt.com.healthcare.Objects.ClientLog;
import healthcare.ckwpjt.com.healthcare.R;

public class LogActivity extends AppCompatActivity {

    private final String mCliendGrp = "Client/";
    private final String mClientId = "KwangwonChoi/";
    private final String mLogPath = "alert_log/";

    private FirebaseDatabase f = FirebaseDatabase.getInstance();
    private DatabaseReference ref = f.getReference(mCliendGrp + mClientId + mLogPath);

    private List<ClientLog> mClientLogs;
    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter listAdapter;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.d("TAG", "onChildAdded:" + dataSnapshot.getKey());

            ClientLog messages = new ClientLog();
            messages.setDate(dataSnapshot.getKey().toString());

            // A new comment has been added, add it to the displayed list
            for(DataSnapshot ds : dataSnapshot.getChildren()) {

                if(ds.getKey().equals("details"))
                    messages.setDetails(ds.getValue().toString());

                if(ds.getKey().equals("state"))
                    messages.setState(ds.getValue().toString());

            }

            mClientLogs.add(messages);
            list.add(cvtLogToItem(messages));
            listAdapter.notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        }


        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    private HashMap<String, String> cvtLogToItem(ClientLog cl){
        HashMap<String,String> item = new HashMap<String,String>();

        item.put("item 1", cl.getState() + " : " + cl.getDetails());
        item.put("item 2", cl.getDate());

        return item;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        ref.addChildEventListener(childEventListener);

        mClientLogs = new ArrayList<ClientLog>();

        ListView listView = findViewById(R.id.log_listview);

        listAdapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
                new String[] {"item 1","item 2"},
                new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(listAdapter);
    }
}
