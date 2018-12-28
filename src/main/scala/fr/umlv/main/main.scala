package fr.umlv.main

import java.io.{PrintWriter, StringWriter}

import fr.umlv.draw.Draw
import fr.umlv.model.Garden
import fr.umlv.parser.Parser
import fr.umlv.typeclasses.Print
import grizzled.slf4j.Logger

import scala.io.Source
import scala.util.{Failure, Success, Try}

object main extends App {

  val logger = Logger("Main Log")
  val filePath = if (!args.isEmpty) "resources/"+ args(0) else "resources/example.txt"

  val tryReadFile = Try(Source.fromFile(filePath)) match {
    case Success(f) => f.getLines().toList
    case Failure(exception) =>  val sw = new StringWriter()
                                exception.printStackTrace(new PrintWriter(sw))
                                logger.error(sw.toString)
                                System.exit(1)
  }

  val lines = tryReadFile.asInstanceOf[List[String]]

  if (lines.isEmpty) {
    logger.error("No line inside the file. Exiting the program")
    System.exit(2)
  }

  val gardenTest = Parser.parseGarden(lines(0).split(" ").toList) match {
    case Some(x) => x
    case _ => logger.error("First line describing the garden is not valid. Exiting the program")
              System.exit(3)
  }

  val garden = gardenTest.asInstanceOf[Garden]
  logger.info("The garden has been created")

  logger.info("Processing the lines for the mowers")
  val mowerLines = if (lines.length % 2 == 0) lines.drop(1).dropRight(1) else lines.drop(1)

  val linesToProcess = mowerLines.sliding(2, 2).toList.map(l => (l(0).split(" ").toList, l(1).toList.map(s => s.toString)))

  val endGarden = Parser.processMowerList(linesToProcess, garden, garden, 1)

  val gardenString = Print.print(endGarden)
  logger.info("\n" + gardenString)


  Draw.drawWindow(endGarden)

/*
  val positionSize = 80
  val width = (garden.topRightPosition.x +1) * positionSize
  val height = (garden.topRightPosition.y +1) * positionSize


  val frame = new JFrame()
  frame.setSize(width, height)

  val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)


  val area = new JComponent {
    override protected def paintComponent (g: Graphics) : Unit = {
      g.drawImage(img, 0, 0, null)
    }

    override def getPreferredSize() : Dimension = {
      new Dimension(width, height)
    }
  }

  val g = img.createGraphics()

  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

  g.setColor(Color.GREEN)
  g.fillRect(0, 0, width, height)
  g.setColor(Color.BLACK)

  for (i <- 1 to garden.topRightPosition.y) {
    g.drawLine(0, i * positionSize, width, i * positionSize)
  }

  for (j <- 1 to garden.topRightPosition.x) {
    g.drawLine(j * positionSize, 0, j * positionSize, height)
  }


  val value : Int = positionSize / 2

  endGarden.mowers.foreach(x => x match {
    case Right(m) =>
      g.setColor(Color.RED)
      g.fillOval((positionSize * m.position.x) + value - 15, (positionSize * (5 - m.position.y)) + value - 15, 30, 30)
      g.setColor(Color.BLACK)
      m.direction match {
        case North =>   g.fillPolygon(Array( (positionSize * m.position.x) + value - 15,(positionSize * m.position.x) + value, (positionSize * m.position.x) + value + 15), Array((positionSize * (5 - m.position.y)) + value - 15, (positionSize * (5 - m.position.y)) + value - 30, (positionSize * (5 - m.position.y)) + value - 15), 3)
        case East =>    g.fillPolygon(Array( (positionSize * m.position.x) + value + 15,(positionSize * m.position.x) + value + 30, (positionSize * m.position.x) + value + 15), Array((positionSize * (5 - m.position.y)) + value - 15, (positionSize * (5 - m.position.y)) + value, (positionSize * (5 - m.position.y)) + value + 15), 3)
        case South =>   g.fillPolygon(Array( (positionSize * m.position.x) + value - 15,(positionSize * m.position.x) + value, (positionSize * m.position.x) + value + 15), Array((positionSize * (5 - m.position.y)) + value + 15, (positionSize * (5 - m.position.y)) + value + 30, (positionSize * (5 - m.position.y)) + value + 15), 3)
        case _ =>       g.fillPolygon(Array( (positionSize * m.position.x) + value - 15,(positionSize * m.position.x) + value - 30, (positionSize * m.position.x) + value - 15), Array((positionSize * (5 - m.position.y)) + value - 15, (positionSize * (5 - m.position.y)) + value, (positionSize * (5 - m.position.y)) + value + 15), 3)

      }
    case _ => Unit
  })


  g.dispose()
  frame.add(area)
  frame.pack()
  frame.setDefaultCloseOperation(2)
  frame.setVisible(true)
*/
}
