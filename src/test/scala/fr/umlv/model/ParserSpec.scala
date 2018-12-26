package fr.umlv.model

import fr.umlv.parser.Parser
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks


class ParserSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  "Parser createGarden" should "return a correct garden" in {
    val l = List("10","10")
    val garden = Parser.createGarden(l).get

    forAll { (n : Int, m : Int) =>
      val l = List(n.toString, m.toString)
      Parser.createGarden(l).get should be(Garden(Coordinate(n,m), List()))

    }

    //garden should be(Garden(Coordinate(10,10), List()))
  }

}
