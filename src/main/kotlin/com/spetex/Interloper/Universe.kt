package com.spetex.Interloper

import arrow.core.Either
import kotlin.math.sqrt

const val GRAVITATIONAL_CONSTANT = 6.674e-11

class Universe {
    private val celestials: MutableMap<String, Celestial> = mutableMapOf()

    fun spawn(mass: Double): Celestial {
        val celestial = createCelestial(mass)
        celestials[celestial.id] = celestial
        return celestial
    }

    fun celestialCount(): Int {
        return celestials.count()
    }

    fun getCelestial(id: String): Either<Unit, Celestial> {
        return Either.fromNullable(celestials[id])
    }

    fun tick(): Unit {
        celestials.forEach { targetCelestial ->
            var finalPull: Vector = listOf(0.0, 0.0, 0.0)
            celestials.filter{ it != targetCelestial } .forEach {
                finalPull = listOf(
                    getPullForce(targetCelestial.value.mass, it.value.mass, targetCelestial.value.coordinates[0], it.value.coordinates[0], getDistance(targetCelestial.value.coordinates, it.value.coordinates)),
                    getPullForce(targetCelestial.value.mass, it.value.mass, targetCelestial.value.coordinates[1], it.value.coordinates[1], getDistance(targetCelestial.value.coordinates, it.value.coordinates)),
                    getPullForce(targetCelestial.value.mass, it.value.mass, targetCelestial.value.coordinates[2], it.value.coordinates[2], getDistance(targetCelestial.value.coordinates, it.value.coordinates)),
                )
            }
            targetCelestial.value.move(listOf(
                finalPull[0] / targetCelestial.value.mass,
                finalPull[1] / targetCelestial.value.mass,
                finalPull[2] / targetCelestial.value.mass,
            ))

        }
    }

    private fun getPullForce(mass1: Double, mass2: Double, cord1: Double, cord2: Double, distance: Double): Double {
        return GRAVITATIONAL_CONSTANT * ((mass1 * mass2) * (cord1 - cord2) / (distance * distance * distance))
    }

    private fun getDistance(coords1: Vector, coords2: Vector): Double {
        val squaredX: Double = (coords1[0] - coords2[0]) * (coords1[0] - coords2[0])
        val squaredY: Double = (coords1[1] - coords2[1]) * (coords1[1] - coords2[1])
        val squaredZ: Double = (coords1[2] - coords2[2]) * (coords1[2] - coords2[2])
        return sqrt(squaredX + squaredY + squaredZ)
    }


    private fun createCelestial(mass: Double): Celestial {
        return Celestial(mass, this)
    }
}