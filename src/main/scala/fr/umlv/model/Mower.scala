package fr.umlv.model

import grizzled.slf4j.Logger

/** A class representing a mower which can be mowed
  *
  * @param position     the position of the mower
  * @param direction    the cardinal direction the mower is facing
  */
case class Mower(position: Coordinate, direction: Direction) {


  val logger = Logger("Mower Log")


  /** Rotates the mower to the right (90 degrees)
    *
    * @return   a mower with the updated direction
    */
  def rotateRight(): Mower = {
    Mower(this.position, this.direction.nextFromRight())
  }

  /** Rotates the mower to the left (90 degrees)
    *
    * @return   a mower with the updated direction
    */
  def rotateLeft(): Mower = {
    Mower(this.position, this.direction.nextFromLeft())
  }


  /** Advances the mower to the next position in front of it. If the next position is invalid (outside of
    * the garden or already occupied), no action will be made
    *
    * @param garden   the garden on which the mower will advance
    * @return         a mower with the updated position
    */
  def advance(garden: Garden): Mower = {
    val newCoord = this.direction match {
      case North => Coordinate(this.position.x, this.position.y + 1)
      case East => Coordinate(this.position.x + 1, this.position.y)
      case South => Coordinate(this.position.x, this.position.y - 1)
      case _ => Coordinate(this.position.x - 1, this.position.y)
    }
    if (garden.isInvalidCoordinate(newCoord)) Mower(newCoord, this.direction)
    else this
  }


  /** Updates the mower on a garden whether it is rotating or advancing. If the action passed is not
    * recognized, no update will be made.
    *
    * @param action   the action to be performed (rotating or advancing)
    * @param garden   the garden on which the mower is being updated
    * @return         a mower with the updated position or direction
    */
  def update(action: String, garden: Garden): Mower = {
    action match {
      case "A" => this.advance(garden);
      case "D" => this.rotateRight();
      case "G" => this.rotateLeft();
      case x =>
        logger.warn("Unknown action \" " + x + " \" detected")
        this
    }
  }
}

object Mower {

  /** Executes multiple updates on a mower.
    *
    * @param actions          the actions to be performed on the mower
    * @param garden           the garden on which the mower is being updated
    * @param accumulator      an accumulator to store the intermediary mowers
    * @return                 a mower on which all the updates have been performed
    */
  def processUpdates(actions: List[String], garden: Garden, accumulator: Mower) : Mower = {
    actions match {
      case x :: xs => processUpdates(xs, garden, accumulator.update (x, garden))
      case _ => accumulator
    }
  }

}
