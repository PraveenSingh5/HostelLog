package com.example.hostellog;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.rejowan.cutetoast.CuteToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Home_Fragment extends Fragment {
    private int REQUEST_CODE = 11;
    private   static  final String CAMERA_PERMISSION =android.Manifest.permission.CAMERA;
   static String email;
    TextView name, mail,status_tv ,room_number1;
    Button logout_btn ,scan;
    GoogleSignInOptions gso;
    GoogleSignInAccount account;
    TextView date_tv,time_tv;
    ShapeableImageView imageView;
     GoogleSignInClient gsc;
    String  dateString;

   static String romm ;


    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        gsc = GoogleSignIn.getClient(requireActivity(),gso);


        logout_btn = view.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        name =view .findViewById(R.id.nameTV);
        mail =view.findViewById(R.id.mailTV);
        status_tv = view.findViewById(R.id.status_tv);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("attendence", MODE_PRIVATE);
        String time = sharedPreferences.getString("attendence", "");

        room_number1 = view.findViewById(R.id.roomNumber_tv);
        imageView = view.findViewById(R.id.profileImage);

        scan = view.findViewById(R.id.scan);
        if(timeRnge()){
            scan.setVisibility(View.VISIBLE);
            scan.setEnabled(true);
        }else {
            scan.setVisibility(View.GONE);
        }
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPermissionDialog();
            }
        });




        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                       Activity activity = getActivity();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                date_tv =view.findViewById(R.id.date_tv);
                                time_tv =view.findViewById(R.id.time_tv);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                                SimpleDateFormat  sdf1 = new SimpleDateFormat("hh-mm-ss a");
                                dateString = sdf.format(date);
                                date_tv.setText(dateString);
                                String   timeString = sdf1.format(date);
                                time_tv.setText(timeString);
                                if (time.equals(dateString)){
                                    status_tv.setText("Present"+"  "+time);
                                    status_tv.setTextColor(getResources().getColor(R.color.green_color));
                                }else {
                                    status_tv.setText("Absent");
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
        if(acct!=null) {
            Glide.with(this).load(acct.getPhotoUrl()).into(imageView);
            email = acct.getEmail();
            name.setText(acct.getDisplayName());
            mail.setText(acct.getEmail());
        }



        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = getStringRequest();
        requestQueue.add(stringRequest);



        return view;
    }

    private void signOut() {
        if (gsc != null) { // Check for null before calling signOut()
            gsc.signOut()
                    .addOnCompleteListener(requireActivity(), task -> {

                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to delete all data?");
                        builder.setIcon(R.drawable.delete_24);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteData();
                                Intent intent = new Intent(requireContext(), MainActivity.class);
                                startActivity(intent);
                                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("logoutes", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putBoolean("isLoggedIn", false);
                                editor.apply();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();


                    });
        } else {
            Log.e("Home_Fragment", "GoogleSignInClient is null, cannot sign out.");
        }
    }




    private void showPermissionDialog() {

        if (ContextCompat.checkSelfPermission(requireContext(),CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED){

            Intent intent = new Intent(getActivity(), ScanAttendences.class);
            startActivity(intent);
        }else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{ CAMERA_PERMISSION  },REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Intent intent = new Intent(getActivity(), ScanAttendences.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }else {

            showPermissionDialog();
        }

    }


    private @NonNull StringRequest getStringRequest() {
        //String url  = "http://172.27.71.8/hostalPhpFile/fatechFormData.php?college_id="+email;
        String url  = "http://172.27.110.186/hostalPhpFile/fatechFormData.php?college_id="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // Process the JSON data here
                            String data = jsonObject.getString("name");
                             romm = jsonObject.getString("room_number");
                            // Update your UI with the fetched data
                            room_number1.setText(romm);
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


    public boolean  timeRnge(){
        Calendar calendar  = Calendar.getInstance();
        int currentHour  = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes  = calendar.get(Calendar.MINUTE);
        int currentSeconds  = calendar.get(Calendar.SECOND);
        int startHour = 1;
        int startMinute = 0;
        int startSecond = 0;
        int endHour = 24;
        int endMinute = 0;
        int endSecond = 0;

        return (currentHour>startHour || (currentHour==startHour && currentMinutes >=startMinute && currentSeconds>=startSecond)) &&
                (currentHour<endHour || (currentHour==endHour && currentMinutes <=endMinute && currentSeconds<=endSecond));
    }



    private void deleteData() {
      //  String url = "http://172.27.71.8/hostalPhpFile/deleteFormData.php?college_id="+email;
        String url = "http://172.27.110.186/hostalPhpFile/deleteFormData.php?college_id="+email;
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CuteToast.ct(requireContext(), response, CuteToast.LENGTH_LONG, CuteToast.SUCCESS, true).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("college_id", email);
                return params;
            }
        };

        queue.add(stringRequest);
    }


}