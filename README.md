# 🎸 Bocchi the Rock! - Live Waifu Polling System

Aplikasi desktop sederhana berbasis **Java Swing** untuk melakukan polling karakter favorit dari anime *Bocchi the Rock!*. Aplikasi ini dilengkapi dengan penyimpanan database lokal menggunakan SQLite, UI Dark Mode, dan grafik hasil yang diperbarui secara *real-time*.

## ✨ Fitur Utama
* **Live Results:** Grafik batang (*bar chart*) hasil voting akan langsung diperbarui (*real-time*) setiap kali suara masuk.
* **Modern Dark Mode UI:** Tampilan antarmuka gelap yang nyaman di mata dengan komponen yang diubahsuai (*custom components*).
* **Responsive Image Scaling:** Gambar karakter akan menyesuaikan ukuran kotak secara otomatis tanpa terpotong atau gepeng (mirip dengan `object-fit: cover` di CSS web).
* **Click-to-Vote:** Pengguna bisa langsung mengklik gambar atau area panel karakter untuk memilih, tidak harus mengklik tepat di *radio button*.
* **Persistent Database:** Menggunakan SQLite terintegrasi (`sqlite-jdbc`). Data voting tidak akan hilang meskipun aplikasi ditutup.
* **Secure Reset:** Tombol untuk mereset seluruh data suara ke angka 0, dilindungi dengan validasi *password* ganda.

## 🛠️ Teknologi yang Digunakan
* **Bahasa:** Java (JDK 8 atau lebih baru)
* **GUI:** Java Swing & AWT
* **Database:** SQLite (File-based database)

## 📁 Struktur Folder & Persiapan Gambar
Agar aplikasi dapat berjalan dengan sempurna, Anda wajib membuat folder bernama `images` di direktori utama (*root*) sejajar dengan folder `src` (atau sejajar dengan file `.exe` jika sudah di-*build*). 

Masukkan 10 file gambar karakter dengan format `.jpg` ke dalam folder `images` dengan nama persis seperti berikut:
1. `Hitori_Gotoh.jpg`
2. `Nijika_Ijichi.jpg`
3.
