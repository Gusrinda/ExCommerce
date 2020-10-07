package com.gusrinda.excommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdminTambahProduk extends AppCompatActivity {

    private TextView judulHalaman;
    private String dataKategoriProduk, dataDeskripsiProduk, dataHargaProduk, dataNamaProduk, saveCurrentDate, saveCurrentTime, produkRandomKey, downloadImageURL;
    private EditText namaProduk, deskripsiProduk, hargaProduk;
    private ImageView fotoProduk;
    private Button btnTambahProduk;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference ProdukImageRef;
    private DatabaseReference ProdukRef;
    private ProgressDialog LoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tambah_produk);

        judulHalaman = findViewById(R.id.nama_Halaman);
        namaProduk = findViewById(R.id.txt_namaProduk);
        deskripsiProduk = findViewById(R.id.txt_deksripsiProduk);
        hargaProduk = findViewById(R.id.txt_hargaProduk);
        fotoProduk = findViewById(R.id.foto_Produk);
        btnTambahProduk = findViewById(R.id.btn_tambahProduk);

        LoadingDialog = new ProgressDialog(this);

        ProdukImageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        ProdukRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);

        dataKategoriProduk = getIntent().getExtras().get("kategori").toString();

        judulHalaman.setText(dataKategoriProduk);

        fotoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        btnTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }



    private void OpenGallery() {
        Intent galleryIntent  = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data!= null)
        {
            ImageUri = data.getData();
            fotoProduk.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData() {
        dataDeskripsiProduk = deskripsiProduk.getText().toString();
        dataHargaProduk = hargaProduk.getText().toString();
        dataNamaProduk = namaProduk.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Foto produk harus ada . . .", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dataDeskripsiProduk))
        {
            Toast.makeText(this, "Tolong isi deksripsi produk", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dataHargaProduk))
        {
            Toast.makeText(this, "Tolong isi harga produk", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dataNamaProduk))
        {
            Toast.makeText(this, "Tolong isi nama produk", Toast.LENGTH_SHORT).show();
        }
        else
            {
                StoreProductInformation();
            }

    }

    private void StoreProductInformation() {

        LoadingDialog.setTitle("Menambahkan Produk Baru");
        LoadingDialog.setMessage("Tolong menunggu produk ditambahkan");
        LoadingDialog.setCanceledOnTouchOutside(false);
        LoadingDialog.show();

        Calendar kalender = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(kalender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(kalender.getTime());

        produkRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProdukImageRef.child(ImageUri.getLastPathSegment() + produkRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            String message = e.toString();
                Toast.makeText(AdminTambahProduk.this, "ERROR : " + message, Toast.LENGTH_SHORT).show();
                LoadingDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminTambahProduk.this, "Gambar produk berhasil didapatkan", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageURL = task.getResult().toString();

                            Toast.makeText(AdminTambahProduk.this, "Produk berhasil diunggah pada database", Toast.LENGTH_SHORT).show();

                            saveInfoProdukDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveInfoProdukDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("idProduk", produkRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("imageUrl", downloadImageURL);
        productMap.put("kategoriProduk", dataKategoriProduk);
        productMap.put("namaProduk", dataNamaProduk);
        productMap.put("hargaProduk", dataHargaProduk);
        productMap.put("deksripsiProduk", dataDeskripsiProduk);

        ProdukRef.child(produkRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AdminTambahProduk.this, AdminKategoriProduk.class);
                    startActivity(intent);
                    LoadingDialog.dismiss();
                    Toast.makeText(AdminTambahProduk.this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        LoadingDialog.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AdminTambahProduk.this, "ERROR : " + message, Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}