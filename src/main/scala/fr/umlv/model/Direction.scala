package fr.umlv.model

sealed trait Direction {
  def nextFromRight() : Direction
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
    def apply(s: String): Direction = s match {
      case "N" => North;
      case "E" => East;
      case "S" => South;
      case "W" => West;

    }
  }