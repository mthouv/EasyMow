package fr.umlv.model

import fr.umlv.typeclasses.Print
import grizzled.slf4j.Logger



/** Class used to represent the garden on which the mowers will move.
  *
  * @param topRightPosition   the coordinates of the top right position in the garden
  *                           (the coordinates of the bottom left position being (0, 0)
  * @param mowers             the list of mowers on the garden
  */
case class Garden(topRightPosition : Coordinate, mowers : List[Mower]) {

  val logger = Logger("Garden Log")


  /** Indicates if a position in the garden is valid or not. To be valid, a position needs to
    * be within the garden and not be occupied by another mower.
    *
    * @param coord    the coordinates of the position to be tested
    * @return         a boolean indicating if the position is valid or not
    */
  def isValidCoordinate(coord : Coordinate): Boolean = {
    val isOccupied = this.mowers.exists(m => m.position == coord)
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
    * @param mower    the mower to be added. It can either be a String describing an error or the mower to be added
    * @return         the new garden with the added mower
    */
  def addMower(mower : Either[String, Mower]): Garden = {
    mower match {
      case Right(m)  => Garden(this.topRightPosition, m :: this.mowers)
      case Left(msg) =>
        logger.warn(msg)
        this
    }
  }
}





