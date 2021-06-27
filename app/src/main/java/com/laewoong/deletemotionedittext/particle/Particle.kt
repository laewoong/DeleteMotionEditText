package com.laewoong.deletemotionedittext.particle

import java.lang.ref.WeakReference

abstract class Particle {
    var originalX = 0
    var centerY = 0
    var x = 0
    var y = 0
    var opacity = 0f
    var vx = 0
    var vy = 0
    var r = 0f
    var pool: WeakReference<MutableList<Particle>?>? = null

    constructor(pool: List<Particle>) {
        this.pool = WeakReference(pool.toMutableList())
    }

    constructor(x: Int, y: Int, opacity: Float, vx: Int, vy: Int, r: Float, centerY: Int) {
        originalX = x
        this.centerY = centerY
        this.x = x
        this.y = y
        this.opacity = opacity
        this.vx = vx
        this.vy = vy
        this.r = r
    }

    abstract fun onMove()

    fun recycle() {
        if (pool != null && pool!!.get() != null) {
            pool!!.get()!!.add(this)
        }
    }
}