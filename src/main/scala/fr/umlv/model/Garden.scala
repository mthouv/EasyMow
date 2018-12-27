package fr.umlv.model

import fr.umlv.typeclasses.Print
import grizzled.slf4j.Logger

final case object InvalidMower

case class Garden(topRightPosition : Coordinate, mowers : List[Either[InvalidMower.type, Mower]]) {

  val logger = Logger("Garden Log")

  def isInvalidCoordinate(coord : Coordinate): Boolean = {
    val isOccupied = this.mowers.exists(e => e match {
      case Right(m) => m.position == coord
      case _ => false
    })

    if (isOccupied) {
      logger.info("Collision detected on position (" + Print.print(coord) + ")")
      false
    }
    else {
      coord.x >= 0 && coord.y >= 0 && coord.x <= this.topRightPosition.x && coord.y <= topRightPosition.y
    }
  }



  def addMower(mower : Either[InvalidMower.type, Mower]): Garden = {
    mower match {
      case Right(m) if (!this.isInvalidCoordinate(m.position)) => this
      case _ => Garden(this.topRightPosition, mower :: this.mowers)
    }
  }
}





