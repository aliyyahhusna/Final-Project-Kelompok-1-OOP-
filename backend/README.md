# Backend Doodle Quest (Spring Boot)

Satu project backend untuk **Auth/User (Zefran)** dan **Score (Aliyyah)**.
Package dasar: `com.kelompok13.doodlequest.backend`
- `user/`  -> register, login, token (Zefran)
- `score/` -> skor dan high score (Aliyyah, buat folder sendiri)

## Cara menjalankan
Syarat: JDK 21 dan Maven. Dari folder `backend/`:

    mvn spring-boot:run

Server jalan di **http://localhost:8080**. Database H2 tersimpan di `backend/data/` (tidak di-commit).
Di IntelliJ: buka `backend/pom.xml` sebagai Maven project, lalu run `DoodleQuestBackendApplication`.

## Endpoint auth
| Method | URL | Body | Hasil sukses | Gagal |
|---|---|---|---|---|
| POST | `/api/auth/register` | `{"username","password"}` | 201 `{"id","username"}` | 400 validasi, 409 username dipakai |
| POST | `/api/auth/login` | `{"username","password"}` | 200 `{"token","username"}` | 401 salah username/password |
| POST | `/api/auth/logout` | header `Authorization: Bearer <token>` | 204 | - |

Aturan: username 3-20 karakter (huruf/angka/underscore, tidak peka huruf besar-kecil), password 6-72 karakter. Password disimpan sebagai hash BCrypt.

Contoh (PowerShell):

    $b = '{"username":"zefran","password":"rahasia123"}'
    Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/auth/register -ContentType 'application/json' -Body $b
    Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/auth/login -ContentType 'application/json' -Body $b

## Untuk Aliyyah (endpoint score)
Client mengirim header `Authorization: Bearer <token>` dari login. Di controller score:

    private final AuthService auth;   // inject lewat constructor
    ...
    User user = auth.requireUser(authorizationHeader);  // 401 otomatis jika tidak valid
    // pakai user.getId() sebagai pemilik skor; JANGAN percaya username dari body request

Token disimpan di memori server, jadi restart backend = pemain harus login ulang.
Jangan mengubah `pom.xml` / `application.properties` bersamaan tanpa kabar ke Zefran.
