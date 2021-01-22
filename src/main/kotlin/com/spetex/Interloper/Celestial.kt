package com.spetex.Interloper

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class Celestial(
    val mass: Double,
    val universe: Universe,
    val id: String = UUID.randomUUID().toString(),
    var coordinates: Vector = listOf(0.0, 0.0, 0.0),
) {

    fun moveBy(vector: Vector): Celestial {
        return Celestial(mass, universe, id, listOf(
            coordinates[0] + vector[0],
            coordinates[1] + vector[1],
            coordinates[2] + vector[2],
        ))
    }

    fun moveTo(position: Vector): Celestial {
        return Celestial(mass, universe, id, position)
    }

    fun getDistanceFrom(target: Celestial): Double {
        val squaredX: Double = (coordinates[0] - target.coordinates[0]) * (coordinates[0] - target.coordinates[0])
        val squaredY: Double = (coordinates[1] - target.coordinates[1]) * (coordinates[1] - target.coordinates[1])
        val squaredZ: Double = (coordinates[2] - target.coordinates[2]) * (coordinates[2] - target.coordinates[2])
        return sqrt(squaredX + squaredY + squaredZ)
    }

    fun getPullForceOf(celestial: Celestial): Vector {
        return listOf(
            GRAVITATIONAL_CONSTANT * ((mass * celestial.mass) * (coordinates[0] - celestial.coordinates[0]) / getDistanceFrom(celestial).pow(3)),
            GRAVITATIONAL_CONSTANT * ((mass * celestial.mass) * (coordinates[1] - celestial.coordinates[1]) / getDistanceFrom(celestial).pow(3)),
            GRAVITATIONAL_CONSTANT * ((mass * celestial.mass) * (coordinates[2] - celestial.coordinates[2]) / getDistanceFrom(celestial).pow(3)),
        )
    }

    fun getGravityPull(): Vector {
        return universe
            .getInfluentialCelestials(this)
            .map { getPullForceOf(it) }
            .reduce { acc: Vector, list: Vector -> listOf(acc[0] + list[0], acc[1] + list[1], acc[2] + list[2]) }
    }

    fun nextState(): Celestial {
        return moveBy(
            getGravityPull()
                .map { it / mass.pow(2) }
        )
    }
}