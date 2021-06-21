package com.example.digital_pallankuzhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.w3c.dom.Text;

import static android.widget.Toast.LENGTH_SHORT;

public class winner extends AppCompatActivity {

    int end;
    String roomid,player1,player2,win,ipaddress;
    TextView winner,statuswin;
    Button again,leaderboard;
    int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        Bundle bundle = getIntent().getExtras();
        ipaddress = ((IpAddress) this.getApplication()).getIp();
        roomid = bundle.getString("roomid");
        end = Integer.parseInt(bundle.getString("end"));
        player1 = bundle.getString("player1");
        player2 = bundle.getString("player2");
        value = Integer.parseInt(bundle.getString("value"));
        again=findViewById(R.id.again);
        leaderboard=findViewById(R.id.leaderboard);
        player1=player1.replace("%20"," ");
        player2=player2.replace("%20"," ");
        winner=findViewById(R.id.winner);
        statuswin=findViewById(R.id.statuswin);
        if(end==1) {
            winner.setText(player1.toUpperCase());
            win=player1;
            if(value==1)
                writewinner();
        }
        else if(end==2) {
            winner.setText(player2.toUpperCase());
            win = player2;
            if(value==2)
                writewinner();
        }
        else {
            winner.setText("");
            statuswin.setText("THE MATCH IS DRAW");
            win = "DRAW";
            if(value==1) {
                writewinner();
            }
        }
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(winner.this,MainActivity.class);
                startActivity(i);
            }
        });
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(winner.this,leaderboard.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void writewinner()
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[3];
                field[0] = "p1";
                field[1] = "p2";
                field[2] = "win";
                //Creating array for data
                String[] data = new String[3];
                data[0] = player1;
                data[1] = player2;
                data[2] = win;
                PutData putData = new PutData(ipaddress+"/leaderboardwrite.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Written Successfully!!"))
                        {

                        }
                        else
                        {
                            Toast.makeText(winner.this, ""+result, LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
