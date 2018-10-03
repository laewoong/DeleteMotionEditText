package com.laewoong.deletemotionedittext.particle;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class Particle {
    public int viewX;
    public int originalX;
    public int centerY;
    public int x;
    public int y;
    public float opacity;
    public int vx;
    public int vy;
    public float r;
    public WeakReference<List<Particle>> pool;

    public Particle(List<Particle> pool) {
        this.pool = new WeakReference<>(pool);
    }

    public Particle(int viewX, int x, int y, float opacity, int vx, int vy, float r, int centerY) {
        this.viewX = viewX;
        this.originalX = x;
        this.centerY = centerY;
        this.x = x;
        this.y = y;
        this.opacity = opacity;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
    }

    public abstract void onMove();

    public void recycle() {
        if(pool != null && pool.get() != null) {
            pool.get().add(this);
        }
    }
}
