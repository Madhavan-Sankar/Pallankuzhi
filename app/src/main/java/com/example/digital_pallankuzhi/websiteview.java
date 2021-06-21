package com.example.digital_pallankuzhi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Annotation;

import static android.widget.Toast.LENGTH_SHORT;

public class websiteview extends AppCompatActivity{
    WebView webView;
    String roomid,ipaddress,value;
    Animation rotateOpen,rotateClose,fromBottom,toBottom;
    com.google.android.material.floatingactionbutton.FloatingActionButton options,reload,exit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_websiteview);
        final Bundle bundle = getIntent().getExtras();
        roomid = bundle.getString("roomid");
        value = bundle.getString("value");
        ipaddress = ((IpAddress) this.getApplication()).getIp();
        webView=findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);

        rotateOpen = AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);
        options = findViewById(R.id.options);
        reload = findViewById(R.id.reload);
        exit = findViewById(R.id.exit);


        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.loadUrl("https://pallankuli.000webhostapp.com/index.php?room_id="+roomid+"&dev="+value);
        webView.setWebViewClient(new WebViewClient());

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String res=url.substring(url.length() - 6);
                if(res.equals("end=_1") || res.equals("end=_2") || res.equals("end=_3")) {
                    Bundle bundle1 = new Bundle();
                    String[] arrSplit = url.split("_");
                    bundle1.putString("player1",arrSplit[1]);
                    bundle1.putString("player2",arrSplit[3]);
                    bundle1.putString("end",arrSplit[5]);
                    bundle1.putString("roomid", roomid);
                    bundle1.putString("value",value);
                    if (url.substring(url.length() - 1).equals("1")) {
                        bundle1.putString("end", "1");
                    } else if (url.substring(url.length() - 1).equals("2")) {
                        bundle1.putString("end", "2");
                    } else if (url.substring(url.length() - 1).equals("3")) {
                        bundle1.putString("end", "3");
                    }
                    Intent intent = new Intent(websiteview.this, winner.class);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                }
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reload.getVisibility()==View.INVISIBLE)
                {
                    reload.setVisibility(View.VISIBLE);
                    exit.setVisibility(View.VISIBLE);
                    reload.startAnimation(fromBottom);
                    exit.startAnimation(fromBottom);
                    options.startAnimation(rotateOpen);
                }
                else
                {
                    reload.setVisibility(View.INVISIBLE);
                    exit.setVisibility(View.INVISIBLE);
                    reload.startAnimation(toBottom);
                    exit.startAnimation(toBottom);
                    options.startAnimation(rotateClose);
                }
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("roomid",roomid);
                bundle1.putString("value",value);
                Intent i = new Intent(websiteview.this,websiteview.class);
                i.putExtras(bundle1);
                startActivity(i);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[1];
                        field[0] = "room_id";
                        //Creating array for data
                        String[] data = new String[1];
                        data[0] = roomid;
                        PutData putData = new PutData(ipaddress+"/delete.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Deleted Successfully!!"))
                                {
                                    Intent intent = new Intent(websiteview.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(websiteview.this, ""+result, LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {

    }

}