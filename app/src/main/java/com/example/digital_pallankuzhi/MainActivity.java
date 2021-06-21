package com.example.digital_pallankuzhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    String ipaddress;
    EditText name;
    Button create,join,leaderboard,htp;
    TextView team;
    String roomid;
    ProgressBar progressBar;
    private static long back_pressed;
    private static final String ALPHA_NUMERIC_STRING = "0123456789abcdefghijklmnopqrstuvwxyz";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //internet
        //        http://192.168.0.112/digital_pallankuzhi
        //        https://pallankuli.000webhostapp.com
        //        http://digitalpallankuzhi.epizy.com  (epizy)
        ((IpAddress) this.getApplication()).setIp("https://pallankuli.000webhostapp.com");//change the IP address here if needed
        ipaddress = ((IpAddress) this.getApplication()).getIp();
        name = findViewById(R.id.name);
        create = findViewById(R.id.create);
        join = findViewById(R.id.join);
        leaderboard=findViewById(R.id.leaderboard);
        team=findViewById(R.id.team);
        htp=findViewById(R.id.htp);
        SpannableString content1 = new SpannableString( "TEAM" ) ;
        content1.setSpan( new UnderlineSpan() , 0 , content1.length() , 0 ) ;
        team.setText(content1) ;
        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final android.app.AlertDialog.Builder alertDialog3 = new android.app.AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View v = inflater.inflate(R.layout.creators, null);  // this line
                alertDialog3.setView(v);
                final android.app.AlertDialog alertDialog = alertDialog3.create();
                alertDialog.show();
                final ImageView back = v.findViewById(R.id.back);
                final ImageButton lnhk=v.findViewById(R.id.lnhk);
                final ImageButton lnbm=v.findViewById(R.id.lnbm);
                final ImageButton lnar=v.findViewById(R.id.lnar);
                final ImageButton lnkm=v.findViewById(R.id.lnkm);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                lnhk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoUrl("http://www.linkedin.com/in/harikrishna-m");
                    }
                });
                lnbm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoUrl("http://www.linkedin.com/in/madhavan-sankar");
                    }
                });
                lnar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoUrl("http://www.linkedin.com/in/aravindh24/");
                    }
                });
                lnkm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoUrl("http://www.linkedin.com/in/kumaresan-r-5975bb151");
                    }
                });

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                alertDialog.getWindow().setAttributes(layoutParams);
            }
        });
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,leaderboard.class);
                startActivity(intent);
            }
        });
        htp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    startDownload();
                    Toast.makeText(MainActivity.this, "Downloading...", LENGTH_SHORT).show();
                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                }
            }
        });
        progressBar=findViewById(R.id.progress);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                final String pname1 = name.getText().toString().trim();
                roomid = randomAlphaNumeric(6);
                if(pname1.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter a name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "room_id";
                            field[1] = "player1";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = roomid;
                            data[1] = pname1;
                            PutData putData = new PutData(ipaddress+"/write.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Written Successfully!!"))
                                    {
                                        final android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(MainActivity.this);
                                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                                        View v = inflater.inflate(R.layout.roomid_display, null);  // this line
                                        alertDialog2.setView(v);
                                        final android.app.AlertDialog alertDialog = alertDialog2.create();
                                        alertDialog.show();
                                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                                        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                        final TextView room_id = v.findViewById(R.id.room_id);
                                        final ImageButton share = v.findViewById(R.id.share);
                                        final Button submit = v.findViewById(R.id.submit);
                                        final Button cancel = v.findViewById(R.id.cancel);
                                        room_id.setText(roomid);
                                        share.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                ClipData clip = ClipData.newPlainText("ROOM ID", roomid);
                                                Toast.makeText(MainActivity.this, "Copied to Clipboard", LENGTH_SHORT).show();
                                                clipboard.setPrimaryClip(clip);
                                                Intent sendIntent = new Intent();
                                                sendIntent.setAction(Intent.ACTION_SEND);
                                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Welcome to Digital Pallankuzhi\nYour room id is : ");
                                                sendIntent.putExtra(Intent.EXTRA_TEXT,roomid);
                                                sendIntent.setType("text/plain");
                                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                                startActivity(shareIntent);
                                            }
                                        });
                                        submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("roomid",roomid);
                                                bundle.putString("value","1");
                                                Intent i = new Intent(MainActivity.this, websiteview.class);
                                                i.putExtras(bundle);
                                                startActivity(i);
                                            }
                                        });
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                alertDialog.dismiss();
                                            }
                                        });
                                        alertDialog.getWindow().setAttributes(layoutParams);
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, ""+result, LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                final String pname2 = name.getText().toString().trim();
                if(pname2.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter a name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    final android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View v = inflater.inflate(R.layout.join_display, null);  // this line
                    alertDialog2.setView(v);
                    final android.app.AlertDialog alertDialog = alertDialog2.create();
                    alertDialog.show();
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    final EditText room_id = v.findViewById(R.id.room_id);
                    final Button submit = v.findViewById(R.id.submit);
                    final Button cancel = v.findViewById(R.id.cancel);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String roomiddialog = room_id.getText().toString();
                            if(roomiddialog.equals(""))
                            {
                                Toast.makeText(MainActivity.this, "Please enter room id!!!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressBar.setVisibility(View.VISIBLE);
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Starting Write and Read data with URL
                                        //Creating array for parameters
                                        String[] field = new String[1];
                                        field[0]="room_id";
                                        //Creating array for data
                                        String[] data = new String[1];
                                        data[0]=roomiddialog;
                                        PutData putData = new PutData(ipaddress+ "/check.php", "POST", field, data);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                progressBar.setVisibility(View.GONE);
                                                String result = putData.getResult();
                                                if (!result.equals("Checked!")) {
                                                    Toast.makeText(MainActivity.this, result, LENGTH_SHORT).show();
                                                } else {
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    Handler handler1 = new Handler(Looper.getMainLooper());
                                                    handler1.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //Starting Write and Read data with URL
                                                            //Creating array for parameters
                                                            String[] field1 = new String[2];
                                                            field1[0]="room_id";
                                                            field1[1]="player2";
                                                            //Creating array for data
                                                            String[] data1 = new String[2];
                                                            data1[0]=roomiddialog;
                                                            data1[1]=pname2;
                                                            PutData putData1 = new PutData(ipaddress+"/update.php","POST",field1,data1);
                                                            if(putData1.startPut()){
                                                                if(putData1.onComplete()){
                                                                    progressBar.setVisibility(View.GONE);
                                                                    String result1 = putData1.getResult();
                                                                    if(result1.equals("Updated"))
                                                                    {
                                                                        Bundle bundle1 = new Bundle();
                                                                        bundle1.putString("roomid",roomiddialog);
                                                                        bundle1.putString("value","2");
                                                                        Intent ii = new Intent(MainActivity.this, websiteview.class);
                                                                        ii.putExtras(bundle1);
                                                                        startActivity(ii);
                                                                    }
                                                                    else{
                                                                        Toast.makeText(MainActivity.this, result1, LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                        //End Write and Read data with URL
                                    }
                                });
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.getWindow().setAttributes(layoutParams);
                }
            }
        });
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startDownload();
            Toast.makeText(MainActivity.this, "Downloading...", LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Permission Denied!!", LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit",
                    Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void gotoUrl(String s)
    {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    public void startDownload()
    {
        String url="https://pallankuli.000webhostapp.com/download/User%20manual%20(Mobile%20App).pdf";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("User manual (Mobile App).pdf");
        request.setDescription("Downloading...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"User manual (Mobile App).pdf");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
