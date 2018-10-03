package com.laewoong.deletemotionedittext.particle;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ParticleFactory {

    private int maxV = 3;
    private int maxRadius = 12;
    private Random random = new Random();

    private final int boundHeightCenter;
    private final int textHeightCenter;
    private final int viewX;
    final private List<Particle> sineMoveParticlePool = new LinkedList<>();
    final private List<Particle> linearMoveParticlePool = new LinkedList<>();


    public ParticleFactory(float viewX, int boundHeightCenter, int textHeightCenter) {
        this.boundHeightCenter = boundHeightCenter;
        this.textHeightCenter = textHeightCenter;
        this.viewX = (int)viewX;
        for(int i=0; i<100; i++) {
            sineMoveParticlePool.add(new SineMoveParticle(sineMoveParticlePool));
        }
    }

    public Particle createSineMoveParticle(int startX) {
        int vx = random.nextInt(maxV)+1;
        int vy = random.nextInt(maxV + 1 + maxV) - maxV;
        if(vy==0) {
            vy = 1;
        }

        Particle particle = null;

        if(sineMoveParticlePool.isEmpty()) {
            particle = new SineMoveParticle(sineMoveParticlePool);
        }
        else {
            particle = sineMoveParticlePool.remove(0);
        }

        particle.viewX = viewX;
        particle.originalX = startX;
        particle.centerY = (boundHeightCenter +
                random.nextInt(textHeightCenter+1+textHeightCenter))-
                textHeightCenter;
        particle.x = startX;
        particle.y = boundHeightCenter;
        particle.opacity = random.nextFloat()+0.5f;
        particle.vx = vx;
        particle.vy = vy;
        particle.r = random.nextInt(maxRadius);


        return particle;

//        return new SineMoveParticle(viewX, startX, boundHeightCenter,
//                random.nextFloat()+0.5f,
//                vx, vy,
//                random.nextInt(maxRadius),
//                (boundHeightCenter +
//                        random.nextInt(textHeightCenter+1+textHeightCenter))-
//                        textHeightCenter);
    }

    public Particle createLinearMoveParticle(int startX) {
        int vx = random.nextInt(maxV)+1;
        int vy = random.nextInt(maxV + 1 + maxV) - maxV;
        if(vy==0) {
            vy = 1;
        }

        Particle particle = null;

        if(linearMoveParticlePool.isEmpty()) {
            particle = new LinearMoveParticle(linearMoveParticlePool);
        }
        else {
            particle = linearMoveParticlePool.remove(0);
        }

        particle.viewX = viewX;
        particle.originalX = startX;
        particle.centerY = (boundHeightCenter +
                random.nextInt(textHeightCenter+1+textHeightCenter))-
                textHeightCenter;
        particle.x = startX;
        particle.y = boundHeightCenter;
        particle.opacity = random.nextFloat()+0.5f;
        particle.vx = vx;
        particle.vy = vy;
        particle.r = random.nextInt(maxRadius);


        return particle;

//        return new LinearMoveParticle(viewX, startX, boundHeightCenter,
//                random.nextFloat()+0.5f,
//                vx, vy,
//                random.nextInt(12),
//                (boundHeightCenter +
//                        random.nextInt(textHeightCenter+1+textHeightCenter))-
//                        textHeightCenter);
    }
}