package com.laewoong.deletemotionedittext.particle

import java.util.*

class ParticleManager {

    private val _particleList = LinkedList<Particle>()
    val particleList:List<Particle> = _particleList

    private lateinit var particleFactory: ParticleFactory

    fun updateBound(
        viewX: Float,
        boundHeightCenter: Int,
        textHeightCenter: Int) {
        particleFactory = ParticleFactory(viewX, boundHeightCenter, textHeightCenter)
    }

    fun createSineMoveParticle(startX: Int) {
        _particleList.add(particleFactory.createSineMoveParticle(startX))
    }

    fun createLinearMoveParticle(startX: Int) {
        _particleList.add(particleFactory.createLinearMoveParticle(startX))
    }

    fun moveParticle() {
        _particleList.forEach {
            it.onMove()
        }

        // Clear invalid particles.
        val i = _particleList.iterator()
        while (i.hasNext()) {
            val particle = i.next()
            if (particle.opacity <= 0f || particle.r <= 0f) {
                i.remove()
                particle.recycle()
            }
        }
    }

    fun clearParticle() {
        _particleList.clear()
    }
}