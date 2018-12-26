package fr.umlv.model

import org.scalacheck.Gen
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class GardenSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "Garden isValidCoordinate" should "return true when the coordinates are within the range of the garden" in {

    val garden = Garden(Coordinate(10,10), List())

    val allCoordinates = for {
      n <- Gen.choose(0, 10)
      m <- Gen.choose(0,10)
    } yield (n,m)

    forAll(allCoordinates) { case (n, m) =>
        garden.isInvalidCoordinate(Coordinate(n, m)) should be(true)
    }
  }


  "Garden isValidCoordinate" should "return false when the coordinates are not within the range of the garden" in {
    val garden = Garden(Coordinate(10,10), List())

    val allCoordinates = for {
      n <- Gen.choose(-1000, 1000) suchThat (x => x < 0 || x > 10)
      m <- Gen.choose(-1000,1000) suchThat (x => x < 0 || x > 10)
    } yield (n,m)

    forAll(allCoordinates) { case (n, m) =>
      garden.isInvalidCoordinate(Coordinate(n, m)) should be(false)
    }

  }


  "Garden addMower" should "add new valid mowers to the garden" in {

    val garden = Garden(Coordinate(10,10), List())

    val m1 = Right(Mower(Coordinate(2,2), North))
    val m2 = Right(Mower(Coordinate(4,2), East))
    val m3 = Right(Mower(Coordinate(0,2), South))

    val finalGarden = garden.addMower(m1).addMower(m2).addMower(m3)

    finalGarden.mowers(0) should be(m3)
    finalGarden.mowers(1) should be(m2)
    finalGarden.mowers(2) should be(m1)
  }


  "Garden addMower" should "add valid mowers and invalidMowers to the garden" in {

    val garden = Garden(Coordinate(10,10), List())

    val m1 = Right(Mower(Coordinate(2,2), North))
    val m2 = Right(Mower(Coordinate(4,2), East))
    val m3 = Right(Mower(Coordinate(0,2), South))

    val finalGarden = garden.addMower(m1).addMower(Left(InvalidMower)).addMower(m2).addMower(m3).addMower(Left(InvalidMower))

    finalGarden.mowers(0) should be(Left(InvalidMower))
    finalGarden.mowers(1) should be(m3)
    finalGarden.mowers(2) should be(m2)
    finalGarden.mowers(3) should be(Left(InvalidMower))
    finalGarden.mowers(4) should be(m1)
  }

}
