package com.gusrinda.excommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gusrinda.excommerce.Model.Users;
import com.gusrinda.excommerce.Prevalent.Prevalent;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginAdmin extends AppCompatActivity {

    private Button btn_login;
    private TextView btn_User;
    private EditText txtNomor, txtPassword;
    private CheckBox checkBoxRemember;
    private ProgressDialog LoadingDialog;

    private String parentDB = "Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        btn_login = findViewById(R.id.btn_Login);
        txtNomor = findViewById(R.id.txt_LoginPhoneNumber);
        txtPassword = findViewById(R.id.txt_LoginPassword);
        checkBoxRemember = findViewById(R.id.checkbox_Remember);
        btn_User = findViewById(R.id.btn_SayaUser);

        LoadingDialog = new ProgressDialog(this);

        Paper.init(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        btn_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAdmin.this, LoginActivity.class));
            }
        });

    }

    private void loginUser() {
        String NomorPelanggan = txtNomor.getText().toString();
        String PasswordPelanggan = txtPassword.getText().toString();

        if (TextUtils.isEmpty(NomorPelanggan)){
            Toast.makeText(this, "Tolong isi nomor telepon anda . . . ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(PasswordPelanggan)){
            Toast.makeText(this, "Tolong isi password anda . . . ", Toast.LENGTH_SHORT).show();
        }
        else {
            LoadingDialog.setTitle("Verifikasi Akun");
            LoadingDialog.setMessage("Tolong menunggu akun diverifikasi");
            LoadingDialog.setCanceledOnTouchOutside(false);
            LoadingDialog.show();

            verifyAccount(NomorPelanggan, PasswordPelanggan);

        }

    }

    private void verifyAccount(final String NomorPelanggan, final String PasswordPelanggan) {
        if (checkBoxRemember.isChecked()){
            Paper.book().write(Prevalent.NomorPelangganKey, NomorPelanggan);
            Paper.book().write(Prevalent.PasswordPelangganKey, PasswordPelanggan);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDB).child(NomorPelanggan).exists())
                {
                    Users userData = dataSnapshot.child(parentDB).child(NomorPelanggan).getValue(Users.class);
                    if (userData.getNomor_Pelanggan().equals(NomorPelanggan))
                    {
                        if (userData.getPassword_Pelanggan().equals(PasswordPelanggan))
                        {
                            Toast.makeText(LoginAdmin.this, "Login Admin Berhasil", Toast.LENGTH_SHORT).show();
                            LoadingDialog.dismiss();
                            Toast.makeText(LoginAdmin.this, "Selamat datang Admin !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginAdmin.this, AdminKategoriProduk.class);
                            startActivity(intent);
                        }
                        else
                        {
                            LoadingDialog.dismiss();
                            Toast.makeText(LoginAdmin.this, "Password salah ! masukkan password yang benar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        LoadingDialog.dismiss();
                        Toast.makeText(LoginAdmin.this, "Kesalahan memasukkan nomor pelanggan", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginAdmin.this, "Akun ADMIN dengan nomor " + NomorPelanggan + " belum terdaftar", Toast.LENGTH_SHORT).show();
                    LoadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}