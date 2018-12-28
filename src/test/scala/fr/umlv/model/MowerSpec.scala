package fr.umlv.model

import org.scalacheck.Gen
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class MowerSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "Mower rotateRight" should "return a new Mower with the correct orientation" in {

    val directions = Gen.oneOf(North, East, South, West)
    forAll(directions) { d =>
      val mowerTest = Mower(Coordinate(0,0), d)
      mowerTest.rotateRight() should be(Mower(mowerTest.position, d.nextFromRight()))
    }
  }


  "Mower rotateLeft" should "return a new Mower with the correct orientation" in {

    val directions = Gen.oneOf(North, East, South, West)
    forAll(directions) { d =>
      val mowerTest = Mower(Coordinate(0,0), d)
      mowerTest.rotateLeft() should be(Mower(mowerTest.position, d.nextFromLeft()))
    }
  }


  "Mower advance" should "return a new Mower when the coordinates are valid" in {

    val garden = Garden(Coordinate(3,3), List())

    val mowerNorth = Mower(Coordinate(0,0), North)
    val mowerEast = Mower(Coordinate(0,3), East)
    val mowerSouth = Mower(Coordinate(3,3), South)
    val mowerWest = Mower(Coordinate(3,0), West)

    mowerNorth.advance(garden) should be(Mower(Coordinate(0,1), North))
    mowerEast.advance(garden) should be(Mower(Coordinate(1,3), East))
    mowerSouth.advance(garden) should be(Mower(Coordinate(3,2), South))
    mowerWest.advance(garden) should be(Mower(Coordinate(2,0), West))
  }


  "Mower advance" should "return the same mower when the coordinates are invalid" in {

    val garden = Garden(Coordinate(3,3), List())

    val mowerNorth = Mower(Coordinate(0,3), North)
    val mowerEast = Mower(Coordinate(3,3), East)
    val mowerSouth = Mower(Coordinate(3,0), South)
    val mowerWest = Mower(Coordinate(0,0), West)

    mowerNorth.advance(garden) should be(mowerNorth)
    mowerEast.advance(garden) should be(mowerEast)
    mowerSouth.advance(garden) should be(mowerSouth)
    mowerWest.advance(garden) should be(mowerWest)
  }


  "Mower update" should "return the mower correctly updated" in {
    val garden = Garden(Coordinate(2,2), List())
    val mower = Mower(Coordinate(1,1), North)

    val m1 = mower.update("A", garden)
    m1 should be(Mower(Coordinate(1,2), North))
    m1.update("A", garden) should be(m1)

    m1.update("D", garden) should be(Mower(m1.position, East))
    val m2 = m1.update("D", garden).update("A", garden)
    m2 should be(Mower(Coordinate(2,2), East))
    m2.update("A", garden) should be(m2)


    m2.update("G", garden) should be(Mower(m2.position, North))
    val m3 = m2.update("G", garden).update("G", garden)
    m3 should be(Mower(m2.position, West))
    m3.update("A", garden) should be(Mower(Coordinate(1,2), West))

    val validActions = List("A", "G", "D")
    val invalidActions = Gen.alphaStr suchThat (s => !validActions.contains(s))
    forAll(invalidActions) { x =>
      mower.update(x, garden) should be(mower)
    }
  }


  "Mower multipleUpdates" should "return a new mower correctly updated" in {

    val garden = Garden(Coordinate(2,2), List())
    val mower = Mower(Coordinate(1,1), North)

    val m1 = Mower.processUpdates(List("A", "G", "A" , "D", "D", "A", "G", "A"), garden, mower)
    m1 should be(Mower(Coordinate(1,2), North))

    val m2 = Mower.processUpdates(List("D", "A", "A" , "F", "X", "G", "A", "A", "D"), garden, mower)
    m2 should be(Mower(Coordinate(2,2), East))
  }

}
