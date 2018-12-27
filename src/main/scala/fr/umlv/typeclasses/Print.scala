package fr.umlv.typeclasses

import fr.umlv.model._


object Print {

  /** A template trait typeclass used to get the string representation of objects.
    *
    */
  trait Show[T] {

    /** Gets a string representation of the passed object.
      *
      * @param t      the object from which the string will be produced
      * @return       a string representation of the object
      */
    def show(t : T) : String
  }

  object Show {

    /** Implicit evidence of the implementation of the Show typeclass for Coordinate class
      *
      */
    implicit val CoordinateShow = new Show[Coordinate] {
      def show(c : Coordinate) : String = c.x.toString + " " + c.y.toString
    }


    /** Implicit evidence of the implementation of the Show typeclass for Direction class
      *
      */
    implicit val DirectionShow = new Show[Direction] {
      def show(d : Direction) : String = d match {
        case North => "North"
        case East => "East"
        case South => "South"
        case _ => "West"
      }
    }


    /** Implicit evidence of the implementation of the Show typeclass for Mower class
      *
      */
    implicit val MowerShow = new Show[Mower] {
      def show(mower : Mower) : String = Print.print(mower.position) + " - " + Print.print(mower.direction)
    }


    /** Implicit evidence of the implementation of the Show typeclass for Garden class
      *
      */
    implicit val GardenShow = new Show[Garden] {
      def show(garden : Garden) : String = {
        val firstLine = Print.print(garden.topRightPosition) + "\n"
        val mowersStr = garden.mowers.reverse.map(m =>
            m match {
              case Left(_) => ""
              case Right(mower) => Print.print(mower)
            })
          .filter(s => s!= "")
          .mkString("\n")

        firstLine + mowersStr
      }
    }
  }


  /** Gets the string representation of an object
    *
    * @param t      the object from which the string will be produced
    * @param ev     the implicit evidence that the object implements the Show typeclass
    * @tparam T     template parameter
    * @return       the string representation of the object
    */
  def print[T](t : T)(implicit ev : Show[T]) : String = ev.show(t)
}
