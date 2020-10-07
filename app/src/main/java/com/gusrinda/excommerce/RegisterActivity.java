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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_Daftar;
    private TextView btn_Login;
    private EditText txtNama, txtNomor, txtAlamat, txtPassword, txtPassword2;
    private ProgressDialog LoadingDialog;

    private String parentDB = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_Login = findViewById(R.id.btn_punyaAkun);
        btn_Daftar = findViewById(R.id.btn_Daftar);
        txtNama = findViewById(R.id.txt_namaPelanggan);
        txtNomor = findViewById(R.id.txt_nomorHandphonePelanggan);
        txtAlamat = findViewById(R.id.txt_alamatPelanggan);
        txtPassword = findViewById(R.id.txt_passwordPelanggan);

        LoadingDialog = new ProgressDialog(this);

        btn_Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


    }

    private void CreateAccount() {
        String NamaPelanggan = txtNama.getText().toString();
        String NomorPelanggan = txtNomor.getText().toString();
        String AlamatPelanggan = txtAlamat.getText().toString();
        String PasswordPelanggan = txtPassword.getText().toString();

        if (TextUtils.isEmpty(NamaPelanggan)){
            Toast.makeText(this, "Tolong isi nama anda . . . ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(NomorPelanggan)){
            Toast.makeText(this, "Tolong isi nomor telepon anda . . . ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(AlamatPelanggan)){
            Toast.makeText(this, "Tolong isi alamat anda . . . ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(PasswordPelanggan)){
            Toast.makeText(this, "Tolong isi password anda . . . ", Toast.LENGTH_SHORT).show();
        }
        else {
            LoadingDialog.setTitle("Membuat Akun");
            LoadingDialog.setMessage("Tolong menunggu akun dibuat");
            LoadingDialog.setCanceledOnTouchOutside(false);
            LoadingDialog.show();

            ValidatePhoneNumber(NamaPelanggan, NomorPelanggan, AlamatPelanggan, PasswordPelanggan);

        }
    }

    private void ValidatePhoneNumber(final String namaPelanggan, final String nomorPelanggan, final String alamatPelanggan, final String passwordPelanggan) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(nomorPelanggan).exists()))
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("Nomor_Pelanggan", nomorPelanggan);
                    userDataMap.put("Nama_Pelanggan", namaPelanggan);
                    userDataMap.put("Alamat_Pelanggan", alamatPelanggan);
                    userDataMap.put("Password_Pelanggan", passwordPelanggan);

                    RootRef.child("Users").child(nomorPelanggan).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Akun Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                                        LoadingDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Selamat Bergabung", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        LoadingDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network ERROR : Coba lagi beberapa saat", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    {
                        Toast.makeText(RegisterActivity.this, "Pelanggan dengan nomor " + nomorPelanggan + " telah terdaftar", Toast.LENGTH_SHORT).show();
                        LoadingDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Masukkan nomor telepon yang lain", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}