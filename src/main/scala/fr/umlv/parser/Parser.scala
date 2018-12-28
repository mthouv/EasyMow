package fr.umlv.parser

import fr.umlv.model._
import grizzled.slf4j.Logger

import scala.util.{Failure, Success, Try}

/** Objects containing several methods used to parse lists of strings in order to create mowers and gardens
  *
  */
object Parser {


  val logger = Logger("Parser Log")


  /** Parses a list of strings containing the data necessary to create a new garden (ie the coordinates of
    * the top right position, the list of mowers being automatically initialized)
    *
    * @param args   the list of strings containing the data necessary to create a garden
    * @return       an option containing a garden if the creation has been successful
    *               None otherwise
    */
  def parseGarden(args : List[String]) : Option[Garden] = {
    Try(Garden(Coordinate(args(0).toInt, args(1).toInt), List())).toOption
  }


  /** Parses a list of strings containing the data necessary to create a new mower (ie the coordinates of
    * its position and its cardinal direction)
    *
    * @param args     the list of strings containing the data necessary to create a garden
    * @return         an option containing a mower if the creation has been successful
    *                 None otherwise
    */
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


  /** Parses two lists of Strings in order to initialize a mower and update it in a garden
    *
    * @param startParameters     the list containing the data used to create the initial mower (ie the coordinates of
    *                            its position and its cardinal direction)
    * @param actions             the list containing the actions to be performed by the initial mower
    * @param garden              the garden on which the mower will be updated
    * @return                    either a String describing an error (if the creation has failed or the initial mower
    *                            wasn't within the garden) or the final mower after all the actions have been executed
    */
  def processMower(startParameters : List[String], actions : List[String], garden : Garden) : Either[String , Mower] = {
    val mower = parseMower(startParameters)
    mower match {
      case Some(m) if garden.isValidCoordinate(m.position) => Right(Mower.processUpdates(actions, garden, m))
      case Some(_) =>   Left("Mower's position is outside of the garden")
      case _ =>         Left("Creation parameters for the mowers are invalid")
    }
  }


  /** Processes multiple mowers in a garden
    *
    * @param list             a list of 2-tuples that each contains a list of starting parameters and a list of actions
    *                         to be performed
    * @param accumulator      an accumulator to store the intermediary gardens after each processing of a mower
    * @param step             an indicator to know which mower is being processed (used for logging purposes)
    * @return                 the final garden containing all the processed mowers
    */
  def processMowerList(list : List[(List[String], List[String])], accumulator : Garden, step : Int) : Garden = {
    list match {
      case x :: xs =>
        logger.info("Processing mower number " + step)
        processMowerList(xs, accumulator.addMower(processMower(x._1, x._2, accumulator)), step + 1)
      case Nil => accumulator
    }
  }


}
