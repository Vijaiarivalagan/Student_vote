package com.geforce.vijai.student_vote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Polling extends AppCompatActivity {
    Button poll;String json_url="http://vijai1.eu5.org/sona/get_candidate_details.php";
    String poll_url="http://vijai1.eu5.org/sona/polling.php";
    String[] candidate_name=new String[4];  ;
    String[] candidate_reg=new String[4] ;
    RadioButton can1,can2,can3,can4;
    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    SharedPreferences sp;
    String reg_no,candi_name,candi_reg,polling_data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);

        sp=getSharedPreferences("student",MODE_PRIVATE);
        reg_no=sp.getString("reg_no","reg_no");
        json_url+="?reg_no="+reg_no;



        poll=(Button)findViewById(R.id.submit);
        can1=(RadioButton)findViewById(R.id.cn1);
        can2=(RadioButton)findViewById(R.id.cn2);
        can3=(RadioButton)findViewById(R.id.cn3);
        can4=(RadioButton)findViewById(R.id.cn4);

        JSON_DATA_WEB_CALL();



        poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (can1.isChecked()) {
                     candi_name= can1.getText().toString();
                     candi_reg=candidate_reg[0];

                } else if (can2.isChecked()) {
                    candi_name= can2.getText().toString();
                    candi_reg=candidate_reg[1];

                } else if (can3.isChecked()) {
                    candi_name= can3.getText().toString();
                    candi_reg=candidate_reg[2];

                } else if (can4.isChecked()) {
                    candi_name= can4.getText().toString();
                    candi_reg=candidate_reg[3];

                }
                else{
                    poll.setError("must select one candidate");
                    return;
                }
                polling_data+="?reg_no="+reg_no+"&candidate="+candi_reg;
                poll_url+=polling_data;
                //Toast.makeText(getApplicationContext(),poll_url, Toast.LENGTH_LONG).show(); // print the value of selected super star
                poll.setEnabled(false);
                polling(poll_url);
                poll_url="http://vijai1.eu5.org/sona/polling.php";
            }
        });
    }


    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(json_url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            try {

                json = array.getJSONObject(i);
                candidate_name[i]=json.getString("name");
                candidate_reg[i]=json.getString("reg_no");

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        can1.setText(candidate_name[0]);
        can2.setText(candidate_name[1]);
        can3.setText(candidate_name[2]);
        can4.setText(candidate_name[3]);
    }

       private void polling(String url) {

        Log.d( "url: ",url);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, poll_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    boolean error=jsonObject.getBoolean("error");
                    if(!error){

                        Intent i =new Intent(getApplicationContext(),status.class);
                        //i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                    else {
                        poll.setEnabled(true);
                        Toast.makeText(getApplicationContext(),""+message.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    poll.setEnabled(true);
                    Toast.makeText(getApplicationContext(),"error"+e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                poll.setEnabled(true);
                Toast.makeText(getApplicationContext(),""+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //Toast.makeText(getApplicationContext(),"user login destroy",Toast.LENGTH_SHORT).show();
            poll_url="http://vijai1.eu5.org/sona/polling.php";
    }

}
