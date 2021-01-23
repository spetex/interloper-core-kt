package com.spetex.Interloper

import arrow.core.Either

const val GRAVITATIONAL_CONSTANT = 6.674e-11
val NULL_VECTOR = listOf(0.0, 0.0, 0.0)

class Universe(private var celestials: List<Celestial> = listOf()) {

    fun spawn(celestial: Celestial): Universe {
        return Universe(celestials + celestial)
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

    fun nextState(): Universe {
        return Universe(celestials.map { it.nextState(this) })
    }
}