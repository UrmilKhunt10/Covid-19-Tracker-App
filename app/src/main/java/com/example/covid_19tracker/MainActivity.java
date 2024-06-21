package com.example.covid_19tracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity {
    SearchView searchBar;
    RecyclerView stateRecyclerView;
    ArrayList<StateModel> stateList, searchList;
    RequestQueue requestQueue;
    private static String url = "https://data.covid19india.org/state_district_wise.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchBar = findViewById(R.id.searchBar);
        stateRecyclerView = findViewById(R.id.stateRecyclerView);

        stateList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        fetchFromAPI();

        stateRecyclerView.setHasFixedSize(true);
        stateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("Data Loaded","Recycler View In OnCreate");

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query){
                searchList = new ArrayList<>();
                if(query.length()>0) {
                    for(int i=0;i<stateList.size();i++) {
                        if(stateList.get(i).getStateName().toUpperCase().contains(query.toUpperCase())) {
                            searchList.add(stateList.get(i));
                        }
                    }
                    stateRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    stateRecyclerView.setAdapter(new StateAdapter(MainActivity.this, searchList));
                }
                else{
                    stateRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    stateRecyclerView.setAdapter(new StateAdapter(MainActivity.this, stateList));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList = new ArrayList<>();
                if(newText.length()>0) {
                    for(int i=0;i<stateList.size();i++) {
                        if(stateList.get(i).getStateName().toUpperCase().contains(newText.toUpperCase())) {
                            searchList.add(stateList.get(i));
                        }
                    }
                    stateRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    stateRecyclerView.setAdapter(new StateAdapter(MainActivity.this, searchList));
                }
                else{
                    stateRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    stateRecyclerView.setAdapter(new StateAdapter(MainActivity.this, stateList));
                }
                return false;
            }
        });
    }

    private void fetchFromAPI(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject){
                Iterator<String> stateNames = jsonObject.keys();
                while(stateNames.hasNext())
                {
                    String stateName = stateNames.next();
                    stateList.add(new StateModel(stateName));
                }
                stateRecyclerView.setAdapter(new StateAdapter(MainActivity.this, stateList));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(objectRequest);
    }
}