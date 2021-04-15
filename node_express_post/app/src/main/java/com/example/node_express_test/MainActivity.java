package com.example.node_express_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private TextView label;
    private EditText inputText;
    private EditText inputText2;
    private TextView response;
    private Button buttonGet1;

    private void initViews() {
        label = findViewById(R.id.label);
        inputText = findViewById(R.id.inputText);
        inputText2 = findViewById(R.id.inputText2);
        response = findViewById(R.id.response);
        buttonGet1 = findViewById(R.id.buttonGet1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        buttonGet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                final String url = String.valueOf(inputText.getEditableText());

                Map<String, String> map = new HashMap<>();
                map.put("data", inputText2.getEditableText().toString() );

                JSONObject postData = new JSONObject(map);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject r) {
                        System.out.println(r);
                        response.setText("Http response about getting a web page "+ url  +" is:" + Html.fromHtml("<br><br>"+ r));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                queue.add(jsonObjectRequest);
            }
        });
    }


}