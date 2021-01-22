package com.spetex.Interloper

import arrow.core.getOrHandle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class InterloperApplicationTests {
	lateinit var universe: Universe;
	@BeforeEach
	fun setup() {
		universe = Universe()
	}

	@Test
	fun universeSpawnsAndHoldsCelestials() {
		universe.spawn(0.0)
		universe.spawn(0.0)
		universe.spawn(0.0)
		universe.spawn(0.0)
		universe.spawn(0.0)
		assert(universe.celestialCount() == 5)
	}

	@Test
	fun celestialKnowsWhatUniverseItBelongTo() {
		val celestial1 = universe.spawn(1.0)
		assert(celestial1.universe == universe)
	}

	@Test
	fun getCelestialReturnsLeftWhenAskedAboutNonExistingCelestial() {
		assert(universe.getCelestial("this id does not exist").isLeft())
	}

	@Test
	fun getCelestialReturnsRightWhenAskedAboutExistingCelestial() {
		val celestial1 = universe.spawn(1.0)
		assert(universe.getCelestial(celestial1.id).isRight())
	}

	@Test
	fun getCelestialReturnsCelestialBasedOnId() {
		val celestial1 = universe.spawn(0.1)
		universe.getCelestial(celestial1.id).getOrHandle {
			assert(it.equals(celestial1) )
		}
	}

	@Test
	fun celestialMovesInDirectionOfGravitationalPull() {
		val sun = universe.spawn(1.989e30)
		sun.coordinates = listOf(100.0, 100.0, 100.0)
		val earth = universe.spawn(5.972e24)
		earth.coordinates = listOf(149597870.0, 100.0, 100.0)

		while (earth.coordinates[0] > 149597869.0) {
			universe.tick()
		}

	}

}
