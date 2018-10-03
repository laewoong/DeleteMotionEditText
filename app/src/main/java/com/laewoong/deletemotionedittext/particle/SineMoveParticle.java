package com.laewoong.deletemotionedittext.particle;

import java.util.List;

public class SineMoveParticle extends Particle{

    public SineMoveParticle(List<Particle> pool) {
        super(pool);
    }

    public SineMoveParticle(int viewX, int x, int y, float opacity, int vx, int vy, float r, int centerY) {
        super(viewX, x, y, opacity, vx, vy, r, centerY);
    }

    @Override
    public void onMove() {
        x += vx;
        //if(x < viewX) x = 0;

        y = (int)(100 * Math.sin((x - originalX)*(vx*0.008) + (vx*0.008))) + centerY;
        opacity -= 0.005f;
        r-=0.1f;
    }
}
