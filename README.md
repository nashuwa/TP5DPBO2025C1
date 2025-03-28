# TP5DPBO2025C1
## Spesifikasi Tugas:
1. Hubungkan semua proses CRUD dengan database.
2. Tampilkan dialog/prompt error jika masih ada input yang kosong saat insert/update.
3. Tampilkan dialog/prompt error jika sudah ada NIM yang sama saat insert.
<br><br>Semua method insertData, updateData, dan deleteData akan diubah sehingga tersambung dengan sistem database, dan menambahkan class baru Database.java
<br><br>Menambahkan validasi jika input kosong di dalam insertData dan updateData
![Screenshot 2025-03-28 232107](https://github.com/user-attachments/assets/d7c35a39-ac9f-4010-9cc4-6ed0d4bc1cbf)
<br>Menambahkan validasi untuk mengecek NIM yang sudah ada di method insertData
![Screenshot 2025-03-28 232609](https://github.com/user-attachments/assets/a7328865-f582-402d-8c50-6668c905a063)

## Bukti bahwa database dengan program sudah saling terhubung:
![Screenshot 2025-03-28 225828](https://github.com/user-attachments/assets/cf667a5d-8c26-4084-b832-7ea6e31eecf3)

## Mencoba insert dengan NIM yang sudah terdaftar:
![Screenshot 2025-03-28 225859](https://github.com/user-attachments/assets/a77832a1-b34b-4e2a-8f69-b44e52147576)
<br>Program akan menampilkan pesan error.

## Mencoba insert dengan salah satu field tidak diisi:
![Screenshot 2025-03-28 225919](https://github.com/user-attachments/assets/5e35e9f7-3afb-42a4-8778-1c474d84e908)
<br>Program akan menampilkan pesan error.

## Bukti data masuk ke dalam database:
![Screenshot 2025-03-28 230055](https://github.com/user-attachments/assets/cff7e2c6-20c8-456f-9fc5-400e09f34cb5)

## Mencoba update dengan salah satu field tidak diisi:
![Screenshot 2025-03-28 230134](https://github.com/user-attachments/assets/c40a38a2-0e25-4096-b600-8b058920e690)
<br>Program akan menampilkan pesan error.

## Mencoba delete salah satu data:
![Screenshot 2025-03-28 230156](https://github.com/user-attachments/assets/e7669fe7-7f41-4223-92e9-1948d4db6ac5)
![Screenshot 2025-03-28 230319](https://github.com/user-attachments/assets/e0285567-321d-481d-a7f1-a03234290780)
<br>Data yang dipilih berhasil didelete.
![Screenshot 2025-03-28 230335](https://github.com/user-attachments/assets/b1ccb61c-b66a-4c42-a78c-56de603daf4f)
<br>Database langsung terupdate.
