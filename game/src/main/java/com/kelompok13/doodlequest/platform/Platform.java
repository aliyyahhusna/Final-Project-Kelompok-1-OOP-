
package com.kelompok13.doodlequest.platform;

import com.kelompok13.doodlequest.common.GameObject;
import com.kelompok13.doodlequest.player.Player;

public abstract class Platform extends GameObject {
    protected boolean active = true;

    protected Platform(double x, double y) {
        super(x, y, 80, 14);
    }

    public abstract void onLanded(Player player);

    public boolean isActive() {
        return active;
    }
}
