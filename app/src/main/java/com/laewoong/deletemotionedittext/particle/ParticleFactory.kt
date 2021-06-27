package com.laewoong.deletemotionedittext.particle

import com.laewoong.deletemotionedittext.particle.SineMoveParticle
import com.laewoong.deletemotionedittext.particle.LinearMoveParticle
import java.util.*

class ParticleFactory(
    viewX: Float,
    private val boundHeightCenter: Int,
    private val textHeightCenter: Int
) {
    private val maxV = 3
    private val maxRadius = 12
    private val random = Random()
    private val sineMoveParticlePool: MutableList<Particle> =
        LinkedList<Particle>()
    private val linearMoveParticlePool: LinkedList<Particle> =
        LinkedList<Particle>()

    fun createSineMoveParticle(startX: Int): Particle {
        val vx = random.nextInt(maxV) + 1
        var vy = random.nextInt(maxV + 1 + maxV) - maxV
        if (vy == 0) {
            vy = 1
        }
        var particle: Particle? = null
        particle = if (sineMoveParticlePool.isEmpty()) {
            SineMoveParticle(sineMoveParticlePool)
        } else {
            sineMoveParticlePool.removeAt(0)
        }
        particle.originalX = startX
        particle.centerY = boundHeightCenter +
                random.nextInt(textHeightCenter + 1 + textHeightCenter) -
                textHeightCenter
        particle.x = startX
        particle.y = boundHeightCenter
        particle.opacity = random.nextFloat() + 0.5f
        particle.vx = vx
        particle.vy = vy
        particle.r = random.nextInt(maxRadius).toFloat()
        return particle

//        return new SineMoveParticle(viewX, startX, boundHeightCenter,
//                random.nextFloat()+0.5f,
//                vx, vy,
//                random.nextInt(maxRadius),
//                (boundHeightCenter +
//                        random.nextInt(textHeightCenter+1+textHeightCenter))-
//                        textHeightCenter);
    }

    fun createLinearMoveParticle(startX: Int): Particle {
        val vx = random.nextInt(maxV) + 1
        var vy = random.nextInt(maxV + 1 + maxV) - maxV
        if (vy == 0) {
            vy = 1
        }
        var particle: Particle? = null
        if (linearMoveParticlePool.isEmpty()) {
            particle = LinearMoveParticle(linearMoveParticlePool)
        } else {
            particle = linearMoveParticlePool.removeAt(0)
        }
        particle.originalX = startX
        particle.centerY = boundHeightCenter +
                random.nextInt(textHeightCenter + 1 + textHeightCenter) -
                textHeightCenter
        particle.x = startX
        particle.y = boundHeightCenter
        particle.opacity = random.nextFloat() + 0.5f
        particle.vx = vx
        particle.vy = vy
        particle.r = random.nextInt(maxRadius).toFloat()
        return particle

//        return new LinearMoveParticle(viewX, startX, boundHeightCenter,
//                random.nextFloat()+0.5f,
//                vx, vy,
//                random.nextInt(12),
//                (boundHeightCenter +
//                        random.nextInt(textHeightCenter+1+textHeightCenter))-
//                        textHeightCenter);
    }

    init {
        for (i in 0..99) {
            sineMoveParticlePool.add(SineMoveParticle(sineMoveParticlePool))
        }
    }
}