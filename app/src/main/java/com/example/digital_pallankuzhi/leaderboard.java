package com.example.digital_pallankuzhi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class leaderboard extends AppCompatActivity {
    Button back;
    MyAdapter myAdapter;
    ArrayList<board> products = new ArrayList<>();
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        back=findViewById(R.id.back);
        mListView=findViewById(R.id.listview);
        mListView.setDivider(null);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        String ipaddress = "https://pallankuli.000webhostapp.com";
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(leaderboard.this,MainActivity.class);
                startActivity(i);
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ipaddress+"/leaderboard.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String p1 = object.getString("p1");
                                String p2 = object.getString("p2");
                                String win = object.getString("win");
                                board b = new board(p1,p2,win);
                                products.add(b);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        myAdapter = new MyAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,products);
                        mListView.setAdapter(myAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(leaderboard.this, error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(leaderboard.this).add(stringRequest);
    }

    class MyAdapter extends ArrayAdapter<board> {

        Context context;
        ArrayList<board> rlast;
        public MyAdapter(Context c, int resource,ArrayList<board> last) {
            super(c, resource,last);
            this.context = c;
            rlast=last;
        }
        @SuppressLint("WrongViewCast")
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.cardview, parent, false);
            TextView p1 = row.findViewById(R.id.p1);
            TextView p2 = row.findViewById(R.id.p2);
            TextView win = row.findViewById(R.id.win);

            final board product1 = rlast.get(position);
            // now set our resources on views
            p1.setText(product1.getP1());
            p2.setText(product1.getP2());
            win.setText(product1.getWin());
            return row;
        }
    }

    @Override
    public void onBackPressed() {

    }
}