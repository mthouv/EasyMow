package fr.umlv.typeclass

import fr.umlv.model._
import fr.umlv.typeclasses.Print
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class PrintSpec extends  FlatSpec with Matchers with GeneratorDrivenPropertyChecks {


  "Print print Coordinate" should "print the coordinates" in {
    forAll { (n : Int, m : Int) =>
      Print.print(Coordinate(n, m)) should be(n + " " + m)
    }
  }

  "Print print Direction" should "print the correct direction" in {

    Print.print(North.asInstanceOf[Direction]) should be("North")
    Print.print(East.asInstanceOf[Direction]) should be("East")
    Print.print(South.asInstanceOf[Direction]) should be("South")
    Print.print(West.asInstanceOf[Direction]) should be("West")
  }


  "Print print Mower" should "print the mower" in {

    forAll { (n : Int, m : Int) =>
      val mower = Mower(Coordinate(n, m), North)
      Print.print(mower) should be(n + " " + m + " - " + "North")
    }

    forAll { (n : Int, m : Int) =>
      val mower = Mower(Coordinate(n, m), East)
      Print.print(mower) should be(n + " " + m + " - " + "East")
    }

    forAll { (n : Int, m : Int) =>
      val mower = Mower(Coordinate(n, m), South)
      Print.print(mower) should be(n + " " + m + " - " + "South")
    }

    forAll { (n : Int, m : Int) =>
      val mower = Mower(Coordinate(n, m), West)
      Print.print(mower) should be(n + " " + m + " - " + "West")
    }
  }


  "Print print Garden" should "print the garden with all its mowers" in {

    val garden = Garden(Coordinate(10,10), List())

    val m1 = Mower(Coordinate(0,0), North)
    val m2 = Mower(Coordinate(5,0), South)
    val m3 = Mower(Coordinate(2,8), West)
    val m4 = Mower(Coordinate(9,9), East)
    val m5 = Mower(Coordinate(42, 42), West)

    val finalGarden = garden.addMower(Right(m1)).addMower(Right(m2)).addMower(Left("AAAAAA")).addMower(Right(m3)).addMower(Right(m4))

    val expectedStr = "10 10\n" + Print.print((m1)) + "\n" + Print.print((m2)) + "\n" + Print.print((m3)) + "\n" + Print.print((m4))
    Print.print(finalGarden) should be(expectedStr)
  }


}
