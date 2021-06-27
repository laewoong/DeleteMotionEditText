package com.laewoong.deletemotionedittext.particle

class LinearMoveParticle : Particle {
    constructor(pool: List<Particle>) : super(pool) {}
    constructor(x: Int, y: Int, opacity: Float, vx: Int, vy: Int, r: Float, centerY: Int) : super(
        x,
        y,
        opacity,
        vx,
        vy,
        r,
        centerY
    ) {
    }

    override fun onMove() {
        x += vx
        //if(x < viewX) x = 0;
        y += vy
        opacity -= 0.005f
        r -= 0.1f
    }
}