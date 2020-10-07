package com.gusrinda.excommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gusrinda.excommerce.Model.Users;
import com.gusrinda.excommerce.Prevalent.Prevalent;

import io.paperdb.Paper;

public class Splash extends AppCompatActivity {

    private ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LoadingDialog = new ProgressDialog(this);

        Paper.init(this);

        String NomorToken = Paper.book().read(Prevalent.NomorPelangganKey);
        String PasswordToken = Paper.book().read(Prevalent.PasswordPelangganKey);

        if( NomorToken != "" && PasswordToken != "")
        {
            if (!TextUtils.isEmpty(NomorToken) && !TextUtils.isEmpty(PasswordToken))
            {
                VerivyUser(NomorToken, PasswordToken);

                LoadingDialog.setTitle("Akun telah Logged In");
                LoadingDialog.setMessage("Tunggu Sebentar");
                LoadingDialog.setCanceledOnTouchOutside(false);
                LoadingDialog.show();
            } else {
                Intent intent = new Intent(Splash.this, LoginActivity.class);
                startActivity(intent);
        }
        }
    }

    private void VerivyUser(final String NomorPelanggan, final String PasswordPelanggan) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(NomorPelanggan).exists())
                {
                    Users userData = dataSnapshot.child("Users").child(NomorPelanggan).getValue(Users.class);
                    if (userData.getNomor_Pelanggan().equals(NomorPelanggan))
                    {
                        if (userData.getPassword_Pelanggan().equals(PasswordPelanggan))
                        {
                            Toast.makeText(Splash.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                            LoadingDialog.dismiss();
                            Toast.makeText(Splash.this, "Selamat datang !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Splash.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            LoadingDialog.dismiss();
                            Toast.makeText(Splash.this, "Password salah ! masukkan password yang benar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        LoadingDialog.dismiss();
                        Toast.makeText(Splash.this, "Kesalahan memasukkan nomor pelanggan", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Users userData = dataSnapshot.child("Admins").child(NomorPelanggan).getValue(Users.class);
                    if (userData.getNomor_Pelanggan().equals(NomorPelanggan))
                    {
                        if (userData.getPassword_Pelanggan().equals(PasswordPelanggan))
                        {
                            Toast.makeText(Splash.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                            LoadingDialog.dismiss();
                            Toast.makeText(Splash.this, "Selamat datang !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Splash.this, AdminKategoriProduk.class);
                            startActivity(intent);
                        }
                        else
                        {
                            LoadingDialog.dismiss();
                            Toast.makeText(Splash.this, "Password salah ! masukkan password yang benar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        LoadingDialog.dismiss();
                        Toast.makeText(Splash.this, "Kesalahan memasukkan nomor pelanggan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    }
