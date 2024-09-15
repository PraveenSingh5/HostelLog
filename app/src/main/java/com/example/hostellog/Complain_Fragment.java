package com.example.hostellog;

import static com.example.hostellog.Home_Fragment.email;
import static com.example.hostellog.Home_Fragment.romm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.rejowan.cutetoast.CuteToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Complain_Fragment extends Fragment {
    Spinner spinner2;
    String complain_type;
    RecyclerView recyclerView;
    TextInputEditText complain_text;
    Button summit_complain;
    SwipeRefreshLayout refreshSwipe;
    List<Card_modal_item> items;
    TextView clearAll;

    public Complain_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             View view = inflater.inflate(R.layout.fragment_complain, container, false);
        clearAll = view.findViewById(R.id.clearAll);

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete all data?");
                builder.setIcon(R.drawable.delete_24);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteData();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();


            }
        });
        refreshSwipe = view.findViewById(R.id.refreshSwipe);
          recyclerView  = view.findViewById(R.id.recyclerview);
        summit_complain = view.findViewById(R.id.summit_complain);
        complain_text = view.findViewById(R.id.complain_text);
        spinner2 = view.findViewById(R.id.spinner2);
        ArrayList<String> spinnerlist  = new ArrayList<>();
        spinnerlist.add("none");
        spinnerlist.add("Electrician");
        spinnerlist.add("Furniture");
        spinnerlist.add("other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,spinnerlist);
        spinner2.setAdapter(arrayAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0){
                    Toast.makeText(requireContext(), spinnerlist.get(position), Toast.LENGTH_SHORT).show();
                    complain_type = spinnerlist.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items = new ArrayList<>();
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                StringRequest stringRequest = getStringRequest();
                requestQueue.add(stringRequest);
                refreshSwipe.setRefreshing(false);
            }
        });

        items = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = getStringRequest();
        requestQueue.add(stringRequest);



        summit_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prcesss1(email,romm,complain_type,complain_text.getText().toString());
                complain_text.setText("");
                refreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        items = new ArrayList<>();
                        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                        StringRequest stringRequest = getStringRequest();
                        requestQueue.add(stringRequest);
                        refreshSwipe.setRefreshing(false);
                    }
                });
            }
        });



        return view;
    }


    private void prcesss1(final String college_id ,final String room_number,final String complain_type,final String complain) {
       // String url  = "http://172.27.71.8/hostalPhpFile/studentComplain.php";
        String url  = "http://172.27.110.186/hostalPhpFile/studentComplain.php";
        StringRequest request  = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "this is summit", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("college_id",college_id);
                params.put("room_number",room_number);
                params.put("complain_type",complain_type);
                params.put("complain",complain);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue.add(request);
    }


    private @NonNull StringRequest getStringRequest() {
       // String url  = "http://172.27.71.8/hostalPhpFile/fatechComplainData.php?college_id="+email;
        String url  = "http://172.27.110.186/hostalPhpFile/fatechComplainData.php?college_id="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            for (int i = 0; i <products.length() ; i++) {
                                JSONObject product = products.getJSONObject(i);
                                String c_email = product.getString("college_id");
                                String c_room = product.getString("room_number");
                                String c_complain = product.getString("complain");
                                String c_complain_type = product.getString("complain_type");

                                Card_modal_item card_modal_item = new Card_modal_item(c_email,c_room,c_complain,c_complain_type);
                                items.add(card_modal_item);

                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                            recyclerView.setAdapter(new RecyclerView_Adapter(requireContext(),items));


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




    private void deleteData() {
      //  String url = "http://172.27.71.8/hostalPhpFile/deleteComplainData.php?college_id="+email;
        String url = "http://172.27.110.186/hostalPhpFile/deleteComplainData.php?college_id="+email;
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