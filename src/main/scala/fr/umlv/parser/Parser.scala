package fr.umlv.parser

import fr.umlv.model._
import grizzled.slf4j.Logger

import scala.util.{Failure, Success, Try}

object Parser {

  val logger = Logger("log")


  def parseGarden(args : List[String]) : Option[Garden] = {
    //logger.info("Creating garden")
    return Try(Garden(Coordinate(args(0).toInt, args(1).toInt), List())).toOption
  }



  def parseMower(args : List[String]) : Option[Mower] = {

    val direction = Try(Direction(args(2))) match {
      case Success(d) => d
      case Failure(_) => None
    }

    direction match {
      case Some(x) => Try(Mower(Coordinate(args(0).toInt, args(1).toInt), x.asInstanceOf[Direction])).toOption
      case _ => None
    }
  }



  def processMower(startParameters : List[String], actions : List[String], garden : Garden) : Either[InvalidMower.type , Mower] = {
    val mower = parseMower(startParameters)

    mower match {
      case Some(m) if garden.isInvalidCoordinate(m.position) => Right(Mower.processUpdates(actions, garden, m))
      case _ => Left(InvalidMower)
    }
  }


  def processMowerList(list : List[(List[String], List[String])], garden : Garden, accumulator : Garden) : Garden = {
    list match {
      case x :: xs => processMowerList(xs, garden, accumulator.addMower(processMower(x._1, x._2, accumulator)))
      case Nil => accumulator
    }
  }


}
