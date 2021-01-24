package astone.view.component

import org.scalajs.dom.Event
import org.scalajs.dom.html.{Button, Element, Div}

import scalatags.JsDom.all._

class Collapsible[T <: Element](val element: T) extends Component[T]:
  var collapsed = false

  element.style.transition = "max-height 0.2s ease-out"

  def collapse(): Unit =
    if collapsed then
      element.style.maxHeight = null
      collapsed = false
    else
      println("collapsing")
      element.style.maxHeight = "0px"
      collapsed = true

  def setSize(width: Double, height: Double): Unit =
    element.style.width = s"${width.toInt}px"
    element.style.height = s"${height.toInt}px"
    
extension [T <: Element](element: T)
  def collapsible: Collapsible[T] = new Collapsible(element)
