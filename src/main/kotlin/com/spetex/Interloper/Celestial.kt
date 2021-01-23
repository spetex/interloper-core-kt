package com.spetex.Interloper

import arrow.documented
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class Celestial(
    val mass: Double,
    val id: String = UUID.randomUUID().toString(),
    val coordinates: Vector = listOf(0.0, 0.0, 0.0),
    val speed: Vector = listOf(0.0, 0.0, 0.0)
) {

    fun moveBy(vector: Vector): Celestial {
        return Celestial(mass, id, listOf(
            coordinates[0] + vector[0],
            coordinates[1] + vector[1],
            coordinates[2] + vector[2],
        ), speed)
    }

    fun accelerate(vector: Vector): Celestial {
        return Celestial(mass, id, coordinates, listOf(
            speed[0] + vector[0],
            speed[1] + vector[1],
            speed[2] + vector[2],
        ))
    }

    fun moveTo(position: Vector): Celestial {
        return Celestial(mass, id, position, speed)
    }

    fun move(): Celestial {
        return Celestial(mass, id, listOf(
            coordinates[0] + speed[0],
            coordinates[1] + speed[1],
            coordinates[2] + speed[2],
        ), speed)
    }

    fun nextState(universe: Universe): Celestial {
        return accelerate(getCurrentAcceleration(universe)).move()
    }

    fun getCurrentAcceleration(universe: Universe): Vector {
        return getForceVector(universe).map { it / mass }
    }

    fun getForceVector(universe: Universe): Vector {
        return universe
            .getInfluentialCelestials(this)
            .map { getPullForceOf(it) }
            .reduce { acc: Vector, list: Vector -> listOf(acc[0] + list[0], acc[1] + list[1], acc[2] + list[2]) }
    }

    fun getDistanceFrom(target: Celestial): Double {
        val squaredX: Double = (coordinates[0] - target.coordinates[0]) * (coordinates[0] - target.coordinates[0])
        val squaredY: Double = (coordinates[1] - target.coordinates[1]) * (coordinates[1] - target.coordinates[1])
        val squaredZ: Double = (coordinates[2] - target.coordinates[2]) * (coordinates[2] - target.coordinates[2])
        return sqrt(squaredX + squaredY + squaredZ)
    }

    fun getPullForceOf(celestial: Celestial): Vector {
        return listOf(
            getPullForce(mass, celestial.mass, coordinates[0], celestial.coordinates[0], getDistanceFrom(celestial)),
            getPullForce(mass, celestial.mass, coordinates[1], celestial.coordinates[1], getDistanceFrom(celestial)),
            getPullForce(mass, celestial.mass, coordinates[2], celestial.coordinates[2], getDistanceFrom(celestial)),
        )
    }

    private fun getPullForce(mass1: Double, mass2: Double, coord1: Double, coord2: Double, distance: Double): Double {
        return GRAVITATIONAL_CONSTANT * ((mass1 * mass2) * (coord2 - coord1) / distance.pow(3))
    }
}