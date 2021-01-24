package astone.view.component

import org.scalajs.dom._
import org.scalajs.dom.html.Element

case class Clicked(cx: Double, width: Double)

extension [T <: Component[Element]] (component: T)
  def leftResizableIn(container: Element)(onWidthChanged: Double => Unit): T =
    var clicked: Clicked = null
    var onEdge: Boolean = false

    component.element.onmousedown = e => 
      val width = component.element.getBoundingClientRect.width
      if onEdge then
        clicked = Clicked(e.clientX, width)

    container.onmousemove = e =>
      val boundingRect = component.element.getBoundingClientRect
      val x = e.clientX - boundingRect.left
      if Math.abs(x) < 4 then
        onEdge = true
        component.element.style.cursor = "ew-resize"
      else
        onEdge = false
        component.element.style.cursor = "default"
      if clicked != null then
        val width = clicked.cx - e.clientX + clicked.width
        onWidthChanged(width)

    container.onmouseup = _ => clicked = null

    component