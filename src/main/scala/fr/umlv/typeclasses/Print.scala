package fr.umlv.typeclasses

import fr.umlv.model._

object Print {

  trait Show[T] {
    def show(t : T) : String
  }

  object Show {

    implicit val CoordinateShow = new Show[Coordinate] {
      def show(c : Coordinate) : String = c.x.toString + " " + c.y.toString
    }

    implicit val DirectionShow = new Show[Direction] {
      def show(d : Direction) : String = d match {
        case North => "North"
        case East => "East"
        case South => "South"
        case _ => "West"
      }
    }

    implicit val MowerShow = new Show[Mower] {
      def show(mower : Mower) : String = Print.print(mower.position) + " - " + Print.print(mower.direction)
    }

    implicit val GardenShow = new Show[Garden] {
      def show(garden : Garden) : String = garden.mowers.map(m =>
        m match {
          case Left(_) => ""
          case Right(mower) => Print.print(mower)
        }).mkString("\n")
    }
  }


  def print[T](t : T)(implicit ev : Show[T]) : String = ev.show(t)

}
