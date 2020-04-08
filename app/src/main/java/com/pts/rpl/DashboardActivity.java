package com.pts.rpl;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.pts.rpl.adapter.rv_adapter;
import com.pts.rpl.model.rv_model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private List<rv_model> rv_modelArrayList = new ArrayList<>();
    private rv_adapter rv_adapter1;
    private EditText nama, jenis, text;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("Tugas PTS", MODE_PRIVATE);
        final String username = sharedPreferences.getString("id", "");
        recyclerView = findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        btn = findViewById(R.id.btnadd);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rv_modelArrayList.clear();
                HashMap<String, String> body = new HashMap<>();
                body.put("username", username);
                AndroidNetworking.post("http://192.168.1.2/Ulangan/select.php")
                        .addBodyParameter(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("GZS", "respon : " + response);

                                    JSONArray orders = response.optJSONArray("result");

                                    if (orders == null) return;
                                    System.out.println(orders.length() + "GZS");

                                    for (int i = 0; i < orders.length(); i++) {
                                        final JSONObject aData = orders.optJSONObject(i);
                                        rv_model item = new rv_model();
                                        item.setId(aData.getString("id"));
                                        item.setJenis(aData.getString("jenis"));
                                        item.setKode(aData.getString("kode"));
                                        item.setNama(aData.getString("barang"));

                                        rv_modelArrayList.add(item);
                                    }
                                    rv_adapter1.notifyDataSetChanged();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });

                rv_adapter1 = new rv_adapter(getApplicationContext(), rv_modelArrayList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(rv_adapter1);
                rv_adapter1.setOnItemClickCallback(new rv_adapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(rv_model data) {
                        showSelectedHero(data);
                    }
                });

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        HashMap<String, String> body = new HashMap<>();
        body.put("username", username);
        AndroidNetworking.post("http://192.168.1.2/Ulangan/select.php")
                .addBodyParameter(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("GZS", "respon : " + response);

                            JSONArray orders = response.optJSONArray("result");


                            if (orders == null) return;
                            System.out.println(orders.length() + "gzs");


                            for (int i = 0; i < orders.length(); i++) {
                                final JSONObject aData = orders.optJSONObject(i);
                                rv_model item = new rv_model();
                                item.setId(aData.getString("id"));
                                item.setJenis(aData.getString("jenis"));
                                item.setKode(aData.getString("kode"));
                                item.setNama(aData.getString("barang"));

                                rv_modelArrayList.add(item);
                            }
                            rv_adapter1.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
//        System.out.println(rv_modelArrayList.size()+"alan");

        rv_adapter1 = new rv_adapter(getApplicationContext(), rv_modelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(rv_adapter1);
        rv_adapter1.setOnItemClickCallback(new rv_adapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(rv_model data) {
                showSelectedHero(data);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dlgView = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.dialog_adddata, null);
                final Dialog dialog = new Dialog(DashboardActivity.this, android.R.style.Theme_Material_Dialog);
                text = (EditText) dlgView.findViewById(R.id.etKode1);
                nama = (EditText) dlgView.findViewById(R.id.etNamaUser1);
                jenis = (EditText) dlgView.findViewById(R.id.etJenis1);


                ((LinearLayout) dlgView.findViewById(R.id.divTambah)).setOnClickListener(new View.OnClickListener() {
                    private void doNothing() {

                    }

                    @Override
                    public void onClick(View view) {
                        AndroidNetworking.post("http://192.168.1.2/Ulangan/add.php")
                                .addBodyParameter("barang", nama.getText().toString().trim().toUpperCase())
                                .addBodyParameter("jenis", jenis.getText().toString().trim().toUpperCase())
                                .addBodyParameter("kode", text.getText().toString().trim().toUpperCase())
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("GZS", "respon : " + response);
                                        JSONArray payload = response.optJSONArray("hasil");
                                        Toast.makeText(getApplicationContext(), "Succes ADD", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();


                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                dialog.setContentView(dlgView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    private void showSelectedHero(rv_model hero) {
        Toast.makeText(this, "Kamu memilih " + hero.getNama(), Toast.LENGTH_SHORT).show();
        View dlgView = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.dialog_detail, null);
        final Dialog dialog = new Dialog(DashboardActivity.this, android.R.style.Theme_Material_Dialog);


        text = dlgView.findViewById(R.id.etKode);
        text.setText(hero.getKode());
        nama = dlgView.findViewById(R.id.etNama);
        nama.setText(hero.getNama());
        jenis = dlgView.findViewById(R.id.etJenis);
        jenis.setText(hero.getJenis());
        final String id = hero.getId();


        ((LinearLayout) dlgView.findViewById(R.id.divSave)).setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View view) {
                AndroidNetworking.post("http://192.168.1.2/Ulangan/update.php")
                        .addBodyParameter("id", id)
                        .addBodyParameter("barang", nama.getText().toString().trim().toUpperCase())
                        .addBodyParameter("jenis", jenis.getText().toString().trim().toUpperCase())
                        .addBodyParameter("kode", text.getText().toString().trim().toUpperCase())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("GZS", "respon : " + response);
                                JSONArray payload = response.optJSONArray("hasil");
                                Toast.makeText(getApplicationContext(), "UPdate", Toast.LENGTH_SHORT).show();
                                rv_adapter1.notifyDataSetChanged();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        ((LinearLayout) dlgView.findViewById(R.id.divDelete)).setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View view) {
                AndroidNetworking.post("http://192.168.1.2/Ulangan/delete.php")
                        .addBodyParameter("id", id)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("GZS", "respon : " + response);
                                JSONArray payload = response.optJSONArray("hasil");
                                Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
                                rv_adapter1.notifyDataSetChanged();
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        dialog.setContentView(dlgView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

}
