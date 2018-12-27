package fr.umlv.model

/** A trait used to represent the four cardinal directions: North, East, South and West
  *
  */
sealed trait Direction {

  /** Gets the next cardinal direction from the right
    *
    * @return   the next cardinal direction from the right
    */
  def nextFromRight() : Direction


  /** Gets the next cardinal direction from the left
    *
    * @return   the next cardinal direction from the left
    */
  def nextFromLeft() : Direction
}


case object North extends Direction {
  override def nextFromLeft(): Direction = West
  override def nextFromRight(): Direction = East
}

case object East extends Direction {
  override def nextFromLeft(): Direction = North
  override def nextFromRight(): Direction = South
}

case object South extends Direction {
  override def nextFromLeft(): Direction = East
  override def nextFromRight(): Direction = West
}

case object West extends Direction {
  override def nextFromLeft(): Direction = South
  override def nextFromRight(): Direction = North
}



  object Direction {

    /** Gets the cardinal direction corresponding to a String.
      *
      * @param s    the string corresponding to a cardinal direction
      * @return     An option containing the cardinal direction if the string corresponds to any of the direction
      *             None otherwise
      */
    def apply(s: String): Option[Direction] = s match {
      case "N" => Some(North);
      case "E" => Some(East);
      case "S" => Some(South);
      case "W" => Some(West);
      case _ => None
    }
  }
