package fr.umlv.parser

import fr.umlv.model.{Coordinate, Garden}
import grizzled.slf4j.Logger

import scala.util.Try

object Parser {

  val logger = Logger("log")


  def createGarden(args : List[String]) : Try[Garden] = {
    logger.info("Creating garden")
    return Try(Garden(Coordinate(args(0).toInt, args(1).toInt), List()))
  }

}
