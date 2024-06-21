package com.example.covid_19tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SecondActivity extends AppCompatActivity {
    TextView stateName;
    SearchView searchBar;
    RecyclerView cityRecyclerView;
    ArrayList<Model> cityList, searchList;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        stateName = findViewById(R.id.stateName);
        searchBar = findViewById(R.id.searchBar);
        cityRecyclerView = findViewById(R.id.cityRecyclerView);

        cityList = new ArrayList<>();

        Intent i = getIntent();
        stateName.setText(i.getStringExtra("stateName"));

        requestQueue = Volley.newRequestQueue(this);
        fetchFromAPI();

        cityRecyclerView.setHasFixedSize(true);
        cityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList = new ArrayList<>();
                if(query.length()>0){
                    for(int i=0;i<cityList.size();i++){
                        if(cityList.get(i).getCityName().toUpperCase().contains(query.toUpperCase())){
                            searchList.add(cityList.get(i));
                        }
                    }
                    cityRecyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));
                    cityRecyclerView.setAdapter(new CityAdapter(SecondActivity.this, searchList));
                }
                else{
                    cityRecyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));
                    cityRecyclerView.setAdapter(new CityAdapter(SecondActivity.this, cityList));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList = new ArrayList<>();
                if(newText.length()>0){
                    for(int i=0;i<cityList.size();i++){
                        if(cityList.get(i).getCityName().toUpperCase().contains(newText.toUpperCase())){
                            searchList.add(cityList.get(i));
                        }
                    }
                    cityRecyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));
                    cityRecyclerView.setAdapter(new CityAdapter(SecondActivity.this, searchList));
                }
                else{
                    cityRecyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));
                    cityRecyclerView.setAdapter(new CityAdapter(SecondActivity.this, cityList));
                }
                return false;
            }
        });
    }

    private void fetchFromAPI(){
        String url = "https://data.covid19india.org/state_district_wise.json";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject){
                try {
                    JSONObject state = jsonObject.getJSONObject(stateName.getText().toString());
                    JSONObject districtData = state.getJSONObject("districtData");
                    Iterator<String> cityNames = districtData.keys();
                    while (cityNames.hasNext())
                    {
                        String cityName = cityNames.next();
                        JSONObject cityData = districtData.getJSONObject(cityName);
                        cityList.add(new Model(cityName, cityData.getInt("active"), cityData.getInt("confirmed"),
                                cityData.getInt("deceased"), cityData.getInt("recovered")));
                    }
                    cityRecyclerView.setAdapter(new CityAdapter(SecondActivity.this, cityList));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SecondActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
    }
}