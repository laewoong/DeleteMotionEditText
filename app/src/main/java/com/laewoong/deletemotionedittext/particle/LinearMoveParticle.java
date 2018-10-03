package com.laewoong.deletemotionedittext.particle;

import android.util.Log;

import java.util.List;

public class LinearMoveParticle extends Particle{

    public LinearMoveParticle(List<Particle> pool) {
        super(pool);
    }

    public LinearMoveParticle(int viewX, int x, int y, float opacity, int vx, int vy, float r, int centerY) {
        super(viewX, x, y, opacity, vx, vy, r, centerY);
    }

    @Override
    public void onMove() {
        x += vx;
        //if(x < viewX) x = 0;

        y += vy;
        opacity -= 0.005f;
        r-=0.1f;
    }
}