package fr.umlv.model

final case object InvalidMower

case class Garden(topRightPosition : Coordinate, mowers : List[Either[InvalidMower.type, Mower]]) {

  def isInvalidCoordinate(coord : Coordinate): Boolean = {
    return coord.x >= 0 && coord.y >= 0 && coord.x <= this.topRightPosition.x && coord.y <= topRightPosition.y
  }

  def addMower(mower : Either[InvalidMower.type, Mower]): Garden = {
      Garden(this.topRightPosition, mower :: this.mowers)
  }
}





