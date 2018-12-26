package fr.umlv.model


case class Mower(position: Coordinate, direction: Direction) {

  def rotateRight(): Mower = {
    Mower(this.position, this.direction.nextFromRight())
  }


  def rotateLeft(): Mower = {
    Mower(this.position, this.direction.nextFromLeft())
  }

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

  def update(action: String, garden: Garden): Mower = {
    action match {
      case "A" => this.advance(garden);
      case "D" => this.rotateRight();
      case "G" => this.rotateLeft();
      case _ => this
    }
  }
}

object Mower {

  def multipleUpdates(actions: List[String], garden: Garden, accumulator: Mower) : Mower = {
    actions match {
      case x :: xs => multipleUpdates(xs, garden, accumulator.update (x, garden) )
      case _ => accumulator
    }
  }

}
