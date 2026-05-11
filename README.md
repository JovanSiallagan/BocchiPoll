# 🎸 Bocchi the Rock! - Live Waifu Polling System

Aplikasi desktop interaktif untuk melakukan polling karakter favorit dari anime *Bocchi the Rock!*. Dibangun menggunakan **Java Swing** dengan database **SQLite** agar data hasil voting tersimpan permanen secara lokal.

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

## 🚀 Cara Penggunaan

Anda bisa menjalankan aplikasi ini dengan dua cara, baik sebagai pengguna langsung maupun melalui *source code*:

### Opsi 1: Jalankan Langsung (.exe)
Cara paling mudah jika Anda hanya ingin langsung mencoba aplikasinya.
1. Unduh file `BocchiPoll.exe` beserta folder `images` dari repositori ini.
2. Pastikan file `.exe` dan folder `images` diletakkan berdampingan di dalam satu folder yang sama (misalnya di Desktop).
3. Klik ganda (*double-click*) pada `BocchiPoll.exe` dan aplikasi siap digunakan!

### Opsi 2: Menjalankan lewat Eclipse (Source Code)
Cara ini digunakan jika Anda ingin melihat kode sumbernya atau ikut memodifikasi aplikasinya.
1. *Clone* repositori ini ke laptop Anda:
   ```bash
   git clone [https://github.com/username-anda/nama-repo-anda.git](https://github.com/username-anda/nama-repo-anda.git)
