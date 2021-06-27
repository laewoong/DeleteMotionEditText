package com.laewoong.deletemotionedittext.particle

class SineMoveParticle : Particle {
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
        ox = x
        oy = y
    }

    var ox = 0
    var oy = 0
    override fun onMove() {
        x += vx
        //if(x < viewX) x = 0;
        y = (100 * Math.sin((x - originalX) * (vx * 0.008) + vx * 0.008)).toInt() + centerY
        opacity -= 0.005f
        r -= 0.1f
    }
}