package fr.umlv.model


case class Mower(position : Coordinate, direction : Direction) {

  def rotateRight(): Mower = {
    Mower(this.position, this.direction.nextFromRight())
  }


  def rotateLeft(): Mower = {
    Mower(this.position, this.direction.nextFromLeft())
  }


}
