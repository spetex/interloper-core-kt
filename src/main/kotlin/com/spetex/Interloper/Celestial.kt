package com.spetex.Interloper

import java.util.*

class Celestial(val mass: Double, val universe: Universe) {
    val id: String = UUID.randomUUID().toString()
    var coordinates: Vector = listOf(0.0, 0.0, 0.0)

    fun move(movement: Vector) {
        print("My mass: $mass moving on x axis: ${movement[0]}\n")
        coordinates = listOf(
            coordinates[0] + movement[0],
            coordinates[1] + movement[1],
            coordinates[2] + movement[2],
        )
    }
}