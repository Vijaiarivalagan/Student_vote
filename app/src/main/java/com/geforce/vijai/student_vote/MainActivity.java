package com.geforce.vijai.student_vote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    String reg_no,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=getSharedPreferences("student",MODE_PRIVATE);
        reg_no=sp.getString("reg_no","reg_no");
        //vote_access_url+="?reg_no="+reg_no;

    }

    public void votelink(View view) {
        String vote_access_url="http://vijai1.eu5.org/sona/to_start_vote.php?reg_no="+reg_no;
        check_for_vote(vote_access_url);
        vote_access_url="http://vijai1.eu5.org/sona/to_start_vote.php?";

    }

    public void gotostatus(View view) {
        Intent intent=new Intent(this,status.class);
        startActivity(intent);
    }

    private void check_for_vote(String url) {
        Log.d( "url: ",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    boolean error=jsonObject.getBoolean("error");
                    if(!error)
                    {

                        Intent i =new Intent(getApplicationContext(),Polling.class);
                        startActivity(i);


                    }
                    else {

                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"error"+e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),""+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
