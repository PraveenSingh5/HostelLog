package com.example.hostellog;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.rejowan.cutetoast.CuteToast;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FormActivity extends AppCompatActivity {
    EditText name_et, email_et, et_room, et_roll, et_phone;
    Button button;
    TextView textView;
  //  String url = "http://172.27.71.8/hostalPhpFile/studentForm.php";
    String url = "http://172.27.110.186/hostalPhpFile/studentForm.php";
    SignInCredential credential;
    SignInClient oneTapClient;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_et);
        et_room = findViewById(R.id.et_room);
        et_roll = findViewById(R.id.et_roll);
        et_phone = findViewById(R.id.et_phone);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            name_et.setText(acct.getDisplayName());
            email_et.setText(acct.getEmail());
        }


        button = findViewById(R.id.button_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name_et.getText().toString();
                String email1 = email_et.getText().toString();
                String room1 = et_room.getText().toString();
                String roll1 = et_roll.getText().toString();
                String phone1 = et_phone.getText().toString();
                if (!name1.isEmpty() &&validateFullName() && !email1.isEmpty() && validateEmail() && !room1.isEmpty()&& validateRoom() && !roll1.isEmpty()&& validateRoll() && !phone1.isEmpty()&& validatePhone()){
                    prcesss(name1, email1, room1, roll1, phone1);
                    CuteToast.ct(FormActivity.this, "Successfully Registered", CuteToast.LENGTH_LONG, CuteToast.SUCCESS, true).show();
                    Intent intent = new Intent(FormActivity.this, LocationCheckingActivity.class);
                    startActivity(intent);
                    finish();
                    SharedPreferences preferences1 = getSharedPreferences("MyPrefs2", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putBoolean("flag1",true);
                    editor.apply();
                    attendence(email1,"A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A");
                }else {
                    CuteToast.ct(FormActivity.this, "Something went wrong", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();
                }

            }
        });


    }

    private void prcesss(final String name, final String college_id, final String room_number, final String roll_number, final String phone_number) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                name_et.setText("");
                email_et.setText("");
                et_room.setText("");
                et_roll.setText("");
                et_phone.setText("");
                if (response != null) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                } else {
                    CuteToast.ct(FormActivity.this, "Something went wrong", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                name_et.setText("");
                email_et.setText("");
                et_room.setText("");
                et_roll.setText("");
                et_phone.setText("");
                CuteToast.ct(FormActivity.this, "Something went wrong", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("college_id", college_id);
                map.put("room_number", room_number);
                map.put("roll_number", roll_number);
                map.put("phone_number", phone_number);
                return map;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue.add(request);
    }

    private boolean validateFullName() {
        String val = name_et.getText().toString().trim();
        if (val.isEmpty()) {
            name_et.setError("Field can not be empty");
            return false;
        } else {
            name_et.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email_et.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            email_et.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email_et.setError("Invalid Email!");
            return false;
        } else {
            email_et.setError(null);
//            email.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validatePhone() {
        String val = et_phone.getText().toString().trim();
        String regexStr = "^[0-9]{10}$";
        if (val.isEmpty()) {
            et_phone.setError("Field can not be empty");
            return false;
        } else if (!val.matches(regexStr)) {
            et_phone.setError("Enter valid phone number");
            return false;

        } else {
            et_phone.setError(null);

            return true;
        }
    }

    private boolean validateRoll() {
        String val = et_roll.getText().toString().trim();
        String regexStr = "[0-9][0-9][A-Z]{3}[0-9]{3}";
        if (val.isEmpty()) {
            et_roll.setError("Enter valid Roll number");
            return false;
        } else if (!val.matches(regexStr)) {
            et_roll.setError("No White spaces are allowed!");
            return false;
        } else {
            et_roll.setError(null);
            return true;
        }
    }

    private boolean validateRoom() {

        String val = et_room.getText().toString().trim();
        String regexStr = "[A-Z]{1}[0-9]{3}";
        if (val.isEmpty()) {
            et_room.setError("Enter valid Room number");
            return false;
        } else if (!val.matches(regexStr)) {
            et_room.setError("No White spaces are allowed!");
            return false;
        } else {
            et_room.setError(null);
            return true;
        }

    }



    private void attendence( String college_id, String day_01,String day_02,String day_03,String day_04,String day_05,String day_06,String day_07,String day_08,String day_09,String day_10,String day_11,String day_12,String day_13,String day_14,String day_15,String day_16,String day_17,String day_18,String day_19,String day_20,String day_21,String day_22,String day_23,String day_24,String day_25,String day_26,String day_27,String day_28,String day_29,String day_30,String day_31) {
        String url1 = "http://172.27.110.186/hostalPhpFile/AttendenceDataPhp.php";
      //  String url1 = "http://172.27.71.8/hostalPhpFile/AttendenceDataPhp.php";

        StringRequest request = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                } else {
                    CuteToast.ct(FormActivity.this, "Something went wrong", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                CuteToast.ct(FormActivity.this, "Something went wrong", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("college_id", college_id);
                map.put("day_01", day_01);
                map.put("day_02", day_02);
                map.put("day_03", day_03);
                map.put("day_04", day_04);
                map.put("day_05", day_05);
                map.put("day_06", day_06);
                map.put("day_07", day_07);
                map.put("day_08", day_08);
                map.put("day_09", day_09);
                map.put("day_10", day_10);
                map.put("day_11", day_11);
                map.put("day_12", day_12);
                map.put("day_13", day_13);
                map.put("day_14", day_14);
                map.put("day_15", day_15);
                map.put("day_16", day_16);
                map.put("day_17", day_17);
                map.put("day_18", day_18);
                map.put("day_19", day_19);
                map.put("day_20", day_20);
                map.put("day_21", day_21);
                map.put("day_22", day_22);
                map.put("day_23", day_23);
                map.put("day_24", day_24);
                map.put("day_25", day_25);
                map.put("day_26", day_26);
                map.put("day_27", day_27);
                map.put("day_28", day_28);
                map.put("day_29", day_29);
                map.put("day_30", day_30);
                map.put("day_31", day_31);
                return map;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue.add(request);
    }

}

