package com.gusrinda.excommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import io.paperdb.Paper;

public class AdminKategoriProduk extends AppCompatActivity {
    private Button btnLogout;
    private LinearLayout produkBoba, produkCamilan, produkSusu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kategori_produk);
        btnLogout = findViewById(R.id.btn_Logout);
        produkBoba = findViewById(R.id.produk_Boba);
        produkCamilan = findViewById(R.id.produk_Camilan);
        produkSusu = findViewById(R.id.produk_Susu);

        produkBoba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriProduk.this, AdminTambahProduk.class);
                intent.putExtra("kategori", "Boba");
                startActivity(intent);
            }
        });

        produkCamilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriProduk.this, AdminTambahProduk.class);
                intent.putExtra("kategori", "Camilan");
                startActivity(intent);
            }
        });

        produkSusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKategoriProduk.this, AdminTambahProduk.class);
                intent.putExtra("kategori", "Susu");
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();

                Intent intent = new Intent(AdminKategoriProduk.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}