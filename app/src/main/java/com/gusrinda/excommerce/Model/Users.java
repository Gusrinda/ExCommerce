package com.gusrinda.excommerce.Model;

public class Users {
    private String Nama_Pelanggan, Nomor_Pelanggan, Alamat_Pelanggan, Password_Pelanggan;

    public Users (){}

    public Users(String nama_Pelanggan, String nomor_Pelanggan, String alamat_Pelanggan, String password_Pelanggan) {
        Nama_Pelanggan = nama_Pelanggan;
        Nomor_Pelanggan = nomor_Pelanggan;
        Alamat_Pelanggan = alamat_Pelanggan;
        Password_Pelanggan = password_Pelanggan;
    }

    public String getNama_Pelanggan() {
        return Nama_Pelanggan;
    }

    public void setNama_Pelanggan(String nama_Pelanggan) {
        Nama_Pelanggan = nama_Pelanggan;
    }

    public String getNomor_Pelanggan() {
        return Nomor_Pelanggan;
    }

    public void setNomor_Pelanggan(String nomor_Pelanggan) {
        Nomor_Pelanggan = nomor_Pelanggan;
    }

    public String getAlamat_Pelanggan() {
        return Alamat_Pelanggan;
    }

    public void setAlamat_Pelanggan(String alamat_Pelanggan) {
        Alamat_Pelanggan = alamat_Pelanggan;
    }

    public String getPassword_Pelanggan() {
        return Password_Pelanggan;
    }

    public void setPassword_Pelanggan(String password_Pelanggan) {
        Password_Pelanggan = password_Pelanggan;
    }
}
