package fr.umlv.draw

import java.awt.image.BufferedImage
import java.awt.{Color, Dimension, Graphics, RenderingHints}

import fr.umlv.model._
import javax.swing.{JComponent, JFrame}

object Draw {

  val positionSize = 80
  val circleRadius = 15


  /** Draws a mower in a graphic context.
    *
    * @param mower          the mower to be drawn
    * @param maxOrdinate    the ordinate of the top right position of the garden on which the mower will be drawn
    *                       We have to pass this argument to calculate the ordinates because the origin point of a Graphics context is the
    *                       top left corner (meaning the ordinates are reversed) whereas our origin point is the bottom left corner
    * @param g              the graphic context in which we will draw
    */
  def drawMower(mower : Mower, maxOrdinate : Int, g : Graphics) : Unit = {
    g.setColor(Color.RED)

    val middleSize = positionSize / 2
    val middlePositionAbscissa = (positionSize * mower.position.x) + middleSize
    val middlePositionOrdinate = (positionSize * (maxOrdinate - mower.position.y)) + middleSize

    g.fillOval(middlePositionAbscissa - circleRadius, middlePositionOrdinate - circleRadius, circleRadius * 2, circleRadius * 2)

    g.setColor(Color.BLACK)
    mower.direction match {
      case North =>
          val array1 = Array( middlePositionAbscissa - circleRadius, middlePositionAbscissa, middlePositionAbscissa + circleRadius)
          val array2 = Array( middlePositionOrdinate - circleRadius, middlePositionOrdinate - circleRadius * 2, middlePositionOrdinate - circleRadius)
          g.fillPolygon( array1, array2, 3)
      case East =>
          val array1 = Array( middlePositionAbscissa + circleRadius, middlePositionAbscissa + circleRadius * 2, middlePositionAbscissa + circleRadius)
          val array2 = Array(middlePositionOrdinate - circleRadius, middlePositionOrdinate, middlePositionOrdinate + circleRadius)
          g.fillPolygon( array1, array2, 3)
      case South =>
          val array1 = Array( middlePositionAbscissa - circleRadius, middlePositionAbscissa, middlePositionAbscissa + circleRadius)
          val array2 = Array(middlePositionOrdinate + circleRadius, middlePositionOrdinate + circleRadius * 2, middlePositionOrdinate + circleRadius)
          g.fillPolygon(array1, array2, 3)
      case _ =>
          val array1 = Array( middlePositionAbscissa - circleRadius,middlePositionAbscissa - circleRadius * 2, middlePositionAbscissa - circleRadius)
          val array2 = Array(middlePositionOrdinate - circleRadius, middlePositionOrdinate, middlePositionOrdinate + circleRadius)
          g.fillPolygon(array1, array2, 3)
    }
  }

  /** Draws a mower in a graphic context.
    *
    * @param garden   the garden to be drawn
    * @param g        the graphic context in which we will draw
    */
  def drawGarden(garden : Garden, g : Graphics) : Unit = {

    val width = (garden.topRightPosition.x +1) * positionSize
    val height = (garden.topRightPosition.y +1) * positionSize
    g.setColor(Color.GREEN)
    g.fillRect(0, 0, width, height)
    g.setColor(Color.BLACK)

    for (i <- 1 to garden.topRightPosition.y) {
      g.drawLine(0, i * positionSize, width, i * positionSize)
    }
    for (j <- 1 to garden.topRightPosition.x) {
      g.drawLine(j * positionSize, 0, j * positionSize, height)
    }

    garden.mowers.foreach(x => x match {
      case Right(m) => drawMower(m, garden.topRightPosition.y, g)
      case _ => Unit
    })
  }


  /** Sets up various objects (the frame in which we will draw, the component that will contain our shapes, etc...)
    * and parameters in order to get a graphic context, then draws the garden inside it.
    *
    * @param garden   the garden to be drawn
    */
  def drawWindow(garden : Garden) : Unit = {

    val frame = new JFrame()
    val width = (garden.topRightPosition.x +1) * positionSize
    val height = (garden.topRightPosition.y +1) * positionSize
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

    drawGarden(garden, g)

    g.dispose()
    frame.add(area)
    frame.pack()
    frame.setDefaultCloseOperation(2)
    frame.setVisible(true)
  }
}
