package com.spetex.Interloper

import arrow.core.getOrHandle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

const val MASS_OF_EARTH = 5.972e24
const val MASS_OF_SUN = 1.989e30
const val AU = 1.496e11
const val EARTH_SPEED_RELATIVE_TO_SUN = 29722.0

@SpringBootTest
class InterloperApplicationTests {
	lateinit var universe: Universe;
	@BeforeEach
	fun setup() {
		universe = Universe()
	}

	@Test
	fun universeSpawnsAndHoldsCelestials() {
		assert(universe
			.spawn(Celestial(0.0))
			.spawn(Celestial(0.0))
			.spawn(Celestial(0.0))
			.spawn(Celestial(0.0))
			.spawn(Celestial(0.0))
			.celestialCount() == 5
		)
	}

	@Test
	fun getCelestialReturnsLeftWhenAskedAboutNonExistingCelestial() {
		assert(universe.getCelestial("this id does not exist").isLeft())
	}

	@Test
	fun getCelestialReturnsRightWhenAskedAboutExistingCelestial() {
		assert(universe.spawn(Celestial(1.0, id = "rock")).getCelestial("rock").isRight())
	}

	@Test
	fun getCelestialReturnsCelestialBasedOnId() {
		assert(universe
			.spawn(Celestial(0.1, id = "rock"))
			.getCelestial("rock")
			.getOrHandle { Celestial(0.0) }
			.id == "rock"
		)
	}

	@Test
	fun getDistanceFromReturnsDistanceOfItselfToTargetCelestial() {
		assert(
			Celestial(0.0, coordinates = listOf(0.0, 0.0, 0.0))
				.getDistanceFrom(
					Celestial(0.0, coordinates = listOf(100.0, 0.0, 0.0))
				) == 100.0
		)
	}

	@Test
	fun getPullForceOfReturnsGravitationalPullOfAnotherCelestial() {
		assert(
			Celestial(MASS_OF_EARTH)
				.getPullForceOf(
					Celestial(MASS_OF_SUN, coordinates = listOf(AU, 0.0, 0.0))
				) == listOf(3.5422368558580456E22, 0.0, 0.0)
		)
	}

	@Test
	fun getPullForceOfReturnsGravitationalPullOfAnotherCelestialReversed() {
		assert(
			Celestial(MASS_OF_EARTH, coordinates = listOf(AU, 0.0, 0.0))
				.getPullForceOf(
					Celestial(MASS_OF_SUN)
				) == listOf(-3.5422368558580456E22, 0.0, 0.0)
		)
	}

	@Test
	fun getGravityPullReturnsFinalVectorOfCelestial() {
        val earth = Celestial(MASS_OF_EARTH, coordinates = listOf(AU, 0.0, 0.0))

		assert(earth.getForceVector(
			universe
				.spawn(earth)
				.spawn(Celestial(MASS_OF_SUN, coordinates = listOf(0.0, 0.0, 0.0)))
		) == listOf(-3.5422368558580456E22, 0.0, 0.0))
	}

	@Test
	fun accelerateIncreasesSpeedInDirection() {
		assert(Celestial(MASS_OF_EARTH)
			.moveTo(listOf(0.0, 0.0, 0.0))
			.accelerate(listOf(10.0, 10.0, 10.0))
			.move()
			.move()
			.coordinates == listOf(20.0, 20.0, 20.0)
		)
	}

	@Test
	fun accelerateDecreasesSpeedInDirection() {
		assert(Celestial(MASS_OF_EARTH)
			.moveTo(listOf(0.0, 0.0, 0.0))
			.accelerate(listOf(10.0, 10.0, 10.0))
			.move()
			.move()
			.accelerate(listOf(-10.0, -10.0, -10.0))
			.move()
			.move()
			.coordinates == listOf(20.0, 20.0, 20.0)
		)
	}

	@Test
	fun earthMovesTowardsSun() {
		val earth = Celestial(MASS_OF_EARTH, coordinates = listOf(AU, 0.0, 0.0))
        assert(earth.nextState(
			universe
				.spawn(Celestial(MASS_OF_SUN))
				.spawn(earth)
		).coordinates[0] < AU)
	}

	@Test
	fun earthOrbitsSun() {
		var earth = Celestial(
			MASS_OF_EARTH,
			coordinates = listOf(AU, 0.0, 0.0),
			speed = listOf(0.0,
			EARTH_SPEED_RELATIVE_TO_SUN, 0.0)
		)
		universe = universe
			.spawn(Celestial(MASS_OF_SUN))
			.spawn(earth)

		var i = 0
		// go from zero back to zero on y coordinate is half revolution
		while (earth.coordinates[1] >= 0) {
			i++
			earth = earth.nextState(universe)
		}
		assert(i >= 15672908) // half year in seconds
	}

	@Test
	fun universeNextStateUpdatesAllCelestials() {
		assert(universe
			.spawn(Celestial(10.0, id = "rock", speed = listOf(0.0, 10.0, 0.0)))
			.spawn(Celestial(10.0, id = "ice", coordinates = listOf(10000000.0, 0.0, 0.0)))
			.nextState()
			.nextState()
			.nextState()
			.getCelestial("rock")
			.getOrHandle{ Celestial(0.0) }
			.coordinates[1] == 30.0)
	}
}
