package com.spetex.Interloper

import arrow.core.Either

const val GRAVITATIONAL_CONSTANT = 6.674e-11

class Universe {
    private var celestials: List<Celestial> = listOf()

    fun spawn(mass: Double): Celestial {
        val celestial = createCelestial(mass)
        celestials = celestials + celestial
        return celestial
    }

    fun celestialCount(): Int {
        return celestials.count()
    }

    fun getCelestial(id: String): Either<Unit, Celestial> {
        return Either.fromNullable(celestials.find { it.id == id })
    }

    fun getInfluentialCelestials(celestial: Celestial): List<Celestial> {
        return celestials
            .filter { it.id != celestial.id }
    }

    fun tick() {
        celestials = celestials
            .map { it.nextState() }
    }

    private fun createCelestial(mass: Double): Celestial {
        return Celestial(mass, this)
    }
}