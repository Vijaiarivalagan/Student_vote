package com.geforce.vijai.student_vote;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class status extends AppCompatActivity {
    TextView winner,all;
    String result_winner,result_all;
    //http://vijai1.eu5.org/sona/polling_student_status.php?reg_no=1516102122&can_status=1
    String reg_no,post,result_to_print,winn_result,url_result="http://vijai1.eu5.org/sona/polling_student_status.php?can_status=1";
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        sp =getSharedPreferences("student",MODE_PRIVATE);
        reg_no=sp.getString("reg_no","reg_no");

        winner=(TextView)findViewById(R.id.winner);
        all=(TextView)findViewById(R.id.all_res);
        url_result+="&reg_no="+reg_no;
        Log.d("url_rsult",url_result);
        get_results(url_result);
    }

    private void get_results(String url) {
        Log.i("url_timer",url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("----------------inside on error"+error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){
        for(int i = 0; i<array.length(); i++) {

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                int j=i+1;
                result_to_print+= "\n " + j + "." + json.getString("name") +"   "+ json.getInt("reg_no") + "   " + json.getInt("vote_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {
            JSONObject json = array.getJSONObject(0);
            winn_result+=json.getString("name")+"  "+json.getInt("reg_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result_winner=winn_result.substring(4,winn_result.length());
        result_all=result_to_print.substring(result_to_print.indexOf('\n')+1);
        Log.d("winnn",result_winner);
        winner.setText("The winner is :-   "+result_winner);

        all.setText(result_all+"\n\n");

    }

}
