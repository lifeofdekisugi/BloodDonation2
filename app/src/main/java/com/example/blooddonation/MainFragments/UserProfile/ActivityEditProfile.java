package com.example.blooddonation.MainFragments.UserProfile;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.blooddonation.HomeActivity;
import com.example.blooddonation.LoadingDialog;
import com.example.blooddonation.R;
import com.example.blooddonation.databinding.ActivityEditProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityEditProfile extends AppCompatActivity {

    String token;
    LoadingDialog loadingDialog;
    ActivityEditProfileBinding binding;
    private ArrayList<String> divisions ,districts ,upazilas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        loadingDialog = new LoadingDialog(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("authToken", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        getProfileData();

        getDivisionData();

        divisions = new ArrayList<String>();
        districts = new ArrayList<String>();
        upazilas = new ArrayList<String>();

        binding.spinnerDivision.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDistrictData();
            }
        });

        binding.spinnerDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getUpazilaData();
            }
        });

        spinnerConfig();

        binding.btnEditConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewProfile();
            }
        });
    }

    private void setNewProfile() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://blood.dreamitdevlopment.com/public/api/personal-edit?token=" + token;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: @@@@@@@" +response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    Log.d(TAG, "onResponse:    Status           :     " + status);
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();

                    if (status.equals("success")){
                        Toast.makeText(ActivityEditProfile.this, "Success", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(ActivityEditProfile.this, HomeActivity.class));
                                finish();
                            }
                        }, 1200);
                    }else {
                        Log.d(TAG, "onResponse: @@@@@@@@@@@@@               Error !!!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:    " +error);
            }
        }) {
            @Override
            protected Map<String, String> getParams(){

                String name, number, blood_group, gender, age, weight, division, district, upazila;
                name = binding.etUserName.getText().toString();
                number = binding.etPhoneNumber.getText().toString();
                blood_group = binding.spinnerBloodGroup.getText().toString();
                gender = binding.spinnerGender.getText().toString();
                age = binding.etAge.getText().toString();
                weight = binding.etWeight.getText().toString();
                division = binding.spinnerDivision.getText().toString();
                district = binding.spinnerDistrict.getText().toString();
                upazila = binding.spinnerUpazila.getText().toString();

                Log.d(TAG, "getParams: ..........................." + name + "......" + number +  "......" + blood_group + "....." + gender + "......" + age + "....." +
                        weight + "....." + division + "....." + district + "....." + upazila);

                Map<String, String> params = new HashMap<>();
                params.put("name",name);
                params.put("number",number);
                params.put("blood_group",blood_group);
                params.put("gender",gender);
                params.put("age",age);
                params.put("weight",weight);
                params.put("division",division);
                params.put("district",district);
                params.put("upazila",upazila);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void getProfileData() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://blood.dreamitdevlopment.com/public/api/personal-profile-view?token=" + token;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingDialog.hide();
                //Log.d(TAG, "onResponse: @@@@@@@@@@@@@@@@@               Full Response : " + response);

                try {

                    for (int i = 0; i< response.length(); i++){
                        JSONObject profileObject = response.getJSONObject(i);
                        //JSONObject donationDataObject = profileObject.getJSONObject("blood_donation");


                        binding.etUserName.setText(profileObject.getString("name"));
                        binding.etPhoneNumber.setText(profileObject.getString("number"));
                        binding.spinnerBloodGroup.setText(profileObject.getString("blood_group"));
                        binding.spinnerGender.setText(profileObject.getString("gender"));
                        binding.etAge.setText(profileObject.getString("age"));
                        binding.etWeight.setText(profileObject.getString("weight"));
                        binding.spinnerDivision.setText(profileObject.getString("division"));
                        binding.spinnerDistrict.setText(profileObject.getString("district"));
                        binding.spinnerUpazila.setText(profileObject.getString("upazila"));


                        Log.d(TAG, "onResponse: @@@@@@@@@@           My Object : " + profileObject);
                        //Log.d(TAG, "onResponse: @@@@@@@@@@           Blood Donation : " + donationDataObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonArrayRequest);
    }

    private void getDivisionData() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://blood.dreamitdevlopment.com/public/api/division";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("division");

                            Log.d(TAG, "onResponse: @@@@@@@" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject division = jsonArray.getJSONObject(i);

                                divisions.add(division.getString("bn_name"));
                            }

                            /*for address select*/
                            ArrayAdapter<String> divisionAdapter = new ArrayAdapter<>(ActivityEditProfile.this, android.R.layout.simple_spinner_dropdown_item, divisions);
                            binding.spinnerDivision.setAdapter(divisionAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:    " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void getDistrictData() {
        districts.clear();
        binding.spinnerDistrict.setText(null);

        RequestQueue queue = Volley.newRequestQueue(ActivityEditProfile.this);
        String url = "https://blood.dreamitdevlopment.com/public/api/district?district_id=" + binding.spinnerDivision.getText().toString();

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("district");

                            Log.d(TAG, "onResponse: @@@@@@@" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject division = jsonArray.getJSONObject(i);

                                districts.add(division.getString("bn_name"));
                            }

                            /*for address select*/
                            ArrayAdapter<String> divisionAdapter = new ArrayAdapter<>(ActivityEditProfile.this, android.R.layout.simple_spinner_dropdown_item, districts);
                            binding.spinnerDistrict.setAdapter(divisionAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:    " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void getUpazilaData(){

        upazilas.clear();
        binding.spinnerUpazila.setText(null);

        RequestQueue queue = Volley.newRequestQueue(ActivityEditProfile.this);
        String url = "https://blood.dreamitdevlopment.com/public/api/upazila?district_id=" + binding.spinnerDistrict.getText().toString();

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("upazila");

                            Log.d(TAG, "onResponse: @@@@@@@" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject upazila = jsonArray.getJSONObject(i);

                                upazilas.add(upazila.getString("bn_name"));
                            }

                            /*for address select*/
                            ArrayAdapter<String> upazilaAdapter = new ArrayAdapter<>(ActivityEditProfile.this, android.R.layout.simple_spinner_dropdown_item, upazilas);
                            binding.spinnerUpazila.setAdapter(upazilaAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:    " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void spinnerConfig() {
        // Gender Selector Spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender,
                android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGender.setAdapter(genderAdapter);

        // Blood Group Spinner
        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group,
                android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBloodGroup.setAdapter(bloodGroupAdapter);

        // Division Selection Spinner
//        ArrayAdapter<CharSequence> divisionAdapter = ArrayAdapter.createFromResource(this,
//                R.array.division,
//                android.R.layout.simple_spinner_item);
//        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDivision.setAdapter(divisionAdapter);
    }

}