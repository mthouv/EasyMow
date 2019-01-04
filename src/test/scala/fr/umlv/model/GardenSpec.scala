package fr.umlv.model

import org.scalacheck.Gen
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class GardenSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "Garden isValidCoordinate" should "return true when the coordinates are within the range of the garden" in {

    val garden = Garden(Coordinate(100,100), List())

    val allCoordinates = for {
      n <- Gen.choose(0, 100)
      m <- Gen.choose(0,100)
    } yield (n,m)

    forAll(allCoordinates) { case (n, m) =>
        garden.isValidCoordinate(Coordinate(n, m)) should be(true)
    }
  }


  "Garden isValidCoordinate" should "return false when the coordinates are not within the range of the garden" in {
    val garden = Garden(Coordinate(1000,1000), List())

    val allCoordinates = for {
      n <- Gen.choose(Int.MinValue, Int.MaxValue) suchThat (x => x < 0 || x > 1000)
      m <- Gen.choose(Int.MinValue,Int.MaxValue) suchThat (x => x < 0 || x > 1000)
    } yield (n,m)

    forAll(allCoordinates) { case (n, m) =>
      garden.isValidCoordinate(Coordinate(n, m)) should be(false)
    }
  }


  "Garden isValidCoordinate" should "return false when a mower is already present on the position" in {
    val garden = Garden(Coordinate(1000,1000), List())

    val allCoordinates = for {
      n <- Gen.choose(0, 100)
      m <- Gen.choose(0,100)
    } yield (n,m)

    forAll(allCoordinates) { case(n, m) =>
        val mower = Mower(Coordinate(n, m), North)
        val gardenTmp = garden.addMower(Right(mower))
        gardenTmp.isValidCoordinate(Coordinate(n, m)) should be(false)
    }
  }



  "Garden addMower" should "add new valid mowers to the garden" in {

    val garden = Garden(Coordinate(10,10), List())

    val m1 = Mower(Coordinate(2,2), North)
    val m2 = Mower(Coordinate(4,2), East)
    val m3 = Mower(Coordinate(0,2), South)

    val finalGarden = garden.addMower(Right(m1)).addMower(Right(m2)).addMower(Right(m3))

    finalGarden.mowers(0) should be(m3)
    finalGarden.mowers(1) should be(m2)
    finalGarden.mowers(2) should be(m1)
  }


  "Garden addMower" should "only add valid mowers to the garden" in {

    val garden = Garden(Coordinate(10,10), List())

    val m1 = Mower(Coordinate(2,2), North)
    val m2 = Mower(Coordinate(4,2), East)
    val m3 = Mower(Coordinate(0,2), South)

    val finalGarden = garden.addMower(Right(m1)).addMower(Left("aaaaaaaaa")).addMower(Right(m2)).addMower(Right(m3)).addMower(Left("BBBBBBBBB"))

    finalGarden.mowers(0) should be(m3)
    finalGarden.mowers(1) should be(m2)
    finalGarden.mowers(2) should be(m1)
  }

}
