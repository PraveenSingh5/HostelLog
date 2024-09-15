package com.example.hostellog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.zxing.Result;
import com.rejowan.cutetoast.CuteToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScanAttendences extends AppCompatActivity {
    CodeScanner mCodeScanner;
    String dataMail , room;
    String  email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_attendences);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(acct!=null) {
            email = acct.getEmail();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(getRequest());

        }

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Objects.equals(result.getText(), "watbwe4[q0neeeeee4[ttttq0]n43yyyya44444444444444444w3b[ewbn0bu5")){
                            Toast.makeText(ScanAttendences.this, email +"       "+ dataMail, Toast.LENGTH_SHORT).show();
                            if (email.equals(dataMail)){
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
                                String time =  format.format(calendar.getTime());

                                SimpleDateFormat format1 = new SimpleDateFormat("dd");
                                String time1 =  format1.format(calendar.getTime());
                                String dayValue = "day_"+time1;
                                Toast.makeText(ScanAttendences.this, dayValue, Toast.LENGTH_SHORT).show();
                                updateData(email,dayValue,"P"+"  "+time);

                                SharedPreferences sharedPreferences = getSharedPreferences("attendence",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("attendence",time);
                                editor.apply();
                             }else {
                                Toast.makeText(ScanAttendences.this, "Failure", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(ScanAttendences.this, HomeActivity.class);
                            Toast.makeText(ScanAttendences.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }else {
                          Intent intent = new Intent(ScanAttendences.this, HomeActivity.class);
                            Toast.makeText(ScanAttendences.this, "Failure", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private @NonNull StringRequest getRequest() {
       // String url  = "http://172.27.71.8/hostalPhpFile/fatechFormData.php?college_id="+email;
        String url  = "http://172.27.110.186/hostalPhpFile/fatechFormData.php?college_id="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            dataMail = jsonObject.getString("college_id");
                            room = jsonObject.getString("room_number");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        return stringRequest;
    }


    private void updateData(String collegeId, String dayId, String value) {
        String url = "http://172.27.110.186/hostalPhpFile/updatedata.php";
       // String url = "http://172.27.71.8/hostalPhpFile/updatedata.php";// Replace with your actual URL

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            // Handle response
                            if (success) {
                                // Update successful
                            } else {
                                // Handle error
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("college_id", String.valueOf(collegeId));
                params.put("day_id", dayId);
                params.put("value", value);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue.add(request);
        requestQueue.add(request);
    }

}