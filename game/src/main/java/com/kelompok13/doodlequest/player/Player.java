package com.kelompok13.doodlequest.player;

import com.kelompok13.doodlequest.common.Constants;
import com.kelompok13.doodlequest.common.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Pemain Doodle Quest.
 *
 * Semua posisi memakai KOORDINAT DUNIA (world coordinate), sama seperti
 * platform, musuh, dan boss. Y makin kecil = makin tinggi.
 * Konversi ke layar dilakukan oleh kamera di PlayingState, bukan di sini.
 *
 * Kontrak yang dipakai anggota lain:
 *  - bounce()             -> dipanggil Platform.onLanded(player) (Aliyyah)
 *  - isFalling()          -> dipakai CollisionSystem
 *  - getPreviousBottom()  -> dipakai CollisionSystem (cek "datang dari atas")
 *  - setMovingLeft/Right  -> dipanggil PlayingState dari input keyboard (Adzra)
 *  - spawnAt(x, y)        -> safe spawn saat mulai level / respawn
 */
public class Player extends GameObject {

    // Nilai awal untuk playtest. Tuning lewat playtest, jangan dikali rasio resolusi.
    public static final double GRAVITY = 1500;          // unit/detik^2
    public static final double JUMP_SPEED = 900;        // tinggi lompatan ~ v^2 / (2g) = 270 unit
    public static final double MOVE_SPEED = 360;        // unit/detik
    public static final double MAX_FALL_SPEED = 1400;   // batas kecepatan jatuh
    public static final double INVULNERABLE_SECONDS = 1.2;

    private double velocityX = 0;
    private double velocityY = 0;

    private boolean movingLeft = false;
    private boolean movingRight = false;

    private double previousBottom;
    private double invulnerableTimer = 0;

    public Player(double x, double y) {
        super(x, y, Constants.PLAYER_SIZE, Constants.PLAYER_SIZE);
        previousBottom = getBottom();
    }

    @Override
    public void update(double dt) {
        if (!active || dt <= 0) {
            return;
        }

        // Simpan posisi kaki SEBELUM bergerak, dipakai untuk cek landing/stomp.
        previousBottom = getBottom();

        // Gerak horizontal
        velocityX = 0;
        if (movingLeft) {
            velocityX -= MOVE_SPEED;
        }
        if (movingRight) {
            velocityX += MOVE_SPEED;
        }
        x += velocityX * dt;
        x = Math.max(0, Math.min(Constants.WIDTH - width, x));

        // Gravitasi
        velocityY = Math.min(velocityY + GRAVITY * dt, MAX_FALL_SPEED);
        y += velocityY * dt;

        if (invulnerableTimer > 0) {
            invulnerableTimer = Math.max(0, invulnerableTimer - dt);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (!active) {
            return;
        }

        // Berkedip saat invulnerable supaya pemain tahu sedang kebal.
        if (isInvulnerable() && ((int) (invulnerableTimer * 10)) % 2 == 0) {
            return;
        }

        // Tampilan sementara sampai sprite dari Raqiqah siap.
        gc.setFill(Color.web("#4778c8"));
        gc.fillRoundRect(x, y, width, height, 18, 18);
    }

    /** Lompat otomatis. Dipanggil saat mendarat di platform atau setelah stomp. */
    public void bounce() {
        bounce(1.0);
    }

    /** Lompat dengan pengali, misalnya platform khusus yang memantul lebih tinggi. */
    public void bounce(double multiplier) {
        velocityY = -JUMP_SPEED * multiplier;
    }

    /** Menaruh kaki pemain tepat di atas permukaan (dipakai CollisionSystem sebelum bounce). */
    public void landOn(double surfaceTopY) {
        y = surfaceTopY - height;
    }

    /**
     * Mencoba memberi damage. Mengembalikan true jika damage diterima
     * (lalu pemain kebal sementara), false jika sedang kebal.
     * Pengurangan nyawa TETAP dilakukan CollisionSystem lewat GameManager.
     */
    public boolean takeHit() {
        if (isInvulnerable()) {
            return false;
        }
        invulnerableTimer = INVULNERABLE_SECONDS;
        return true;
    }

    /** Taruh pemain di posisi aman (awal level / respawn). Kakinya di atas surfaceTopY. */
    public void spawnAt(double x, double surfaceTopY) {
        this.x = Math.max(0, Math.min(Constants.WIDTH - width, x));
        this.y = surfaceTopY - height;
        velocityX = 0;
        velocityY = 0;
        movingLeft = false;
        movingRight = false;
        previousBottom = getBottom();
        active = true;
    }

    /** Kebal sementara, misalnya setelah respawn atau selesai transisi level. */
    public void makeInvulnerable(double seconds) {
        invulnerableTimer = Math.max(invulnerableTimer, seconds);
    }

    public void stopMoving() {
        movingLeft = false;
        movingRight = false;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isFalling() {
        return velocityY > 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0;
    }

    public double getPreviousBottom() {
        return previousBottom;
    }

    public double getBottom() {
        return y + height;
    }

    public double getCenterX() {
        return x + width / 2;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
}