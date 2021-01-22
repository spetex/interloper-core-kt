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
	fun getDistanceFromReturnsDistanceOfItselfToTargetCelestial() {
		val celestial1 = universe
			.spawn(0.0)
			.moveTo(listOf(0.0, 0.0, 100.0))

		val celestial2 = universe
			.spawn(0.0)
			.moveTo(listOf(0.0, 0.0, 0.0))

		assert(celestial1.getDistanceFrom(celestial2) == 100.0)
	}

	@Test
	fun getPullForceOfReturnsGravitationalPullOfAnotherCelestial() {
		val sun = universe
			.spawn(1.989e30)
			.moveTo(listOf(100.0, 0.0, 0.0))

		val earth = universe
			.spawn(5.972e24)
			.moveTo(listOf(1.496e11, 0.0, 0.0))

		assert(earth.getPullForceOf(sun) == listOf(-3.5422368605936565E22, 0.0, 0.0))
	}

	@Test
	fun getGravityPullReturnsFinalVectorOfCelestial() {
		val sun = universe
			.spawn(1.989e30)
			.moveTo(listOf(100.0, 0.0, 0.0))

		val earth = universe
			.spawn(5.972e24)
			.moveTo(listOf(1.496e11, 0.0, 0.0))

		assert(earth.getGravityPull() == listOf(-3.5422368605936565E22, 0.0, 0.0))
	}
}
