package fr.umlv.model

import fr.umlv.typeclasses.Print
import grizzled.slf4j.Logger


/** Object used to represent an invalid mower in the garden: either the parameters passed to create it were invalid
  * or the starting position was not within the garden.
  *
  */
final case object InvalidMower


/** Class used to represent the garden on which the mowers will move.
  *
  * @param topRightPosition   the coordinates of the top right position in the garden
  *                           (the coordinates of the bottom left position being (0, 0)
  * @param mowers             a list of either invalid mowers or basic mowers
  */
case class Garden(topRightPosition : Coordinate, mowers : List[Either[InvalidMower.type, Mower]]) {


  val logger = Logger("Garden Log")


  /** Indicates if a position in the garden is valid or not. To be valid, a position needs to
    * be within the garden and not be occupied by another mower.
    *
    * @param coord    the coordinates of the position to be tested
    * @return         a boolean indicating if the position is valid or not
    */
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


  /** Adds a new mower to the garden.
    *
    * @param mower    the mower to be added. It can either be an invalid mower or a
    *                 basic mower
    * @return         the new garden with the added mower
    */
  def addMower(mower : Either[InvalidMower.type, Mower]): Garden = {
    mower match {
      case Right(m) if !this.isInvalidCoordinate(m.position) => this
      case _ => Garden(this.topRightPosition, mower :: this.mowers)
    }
  }
}





