package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button;
    RequestQueue requestQueue;

//    String uri = Uri.parse("https://covid-19-data.p.rapidapi.com/country?format=json&name=").buildUpon().build().toString();

      final  String uri = "https://covid-19-data.p.rapidapi.com/country?format=json&name=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
//        requestQueue = Volley.newRequestQueue(this);

        requestQueue = VolleySingleton.getmInstance(this).getmRequestQueue();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Enter Country First");
                    return;
                }

                 String country = editText.getText().toString();
                String url = uri + country;
                Log.d("MESSAGE:", uri);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String countryName, countryCode;
                        double confirmedCases, recovered, critical, deaths;
                        Log.d("rewa", response.toString());


                        try {
                            JSONArray object = (JSONArray) new JSONTokener(response).nextValue();
                            Log.d("rewa", object.toString());
                            JSONObject object1 = object.getJSONObject(0);
                            countryName = object1.getString("country");
                            countryCode = object1.getString("code");
                            confirmedCases = object1.getDouble("confirmed");
                            recovered = object1.getDouble("recovered");
                            critical = object1.getDouble("critical");
                            deaths = object1.getDouble("deaths");


                            textView.setText("Country: " + countryName + " " + countryCode + "\nConfirmed Cases: " + confirmedCases + "\nRecovered Cases: " + recovered +
                                    "\nCritical Cases: " + critical + "\nDeaths: " + deaths);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("x-rapidapi-host", "covid-19-data.p.rapidapi.com");
                        params.put("x-rapidapi-key", "f89032ef46msh01676527717b21ap155334jsn0118dabe27e1");
                        return params;
                    }

                };

                requestQueue.add(request);

            }
        });

    }
}








