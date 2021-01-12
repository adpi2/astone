package astone.view.component

import org.scalajs.dom._
import org.scalajs.dom.html.Element

class LeftResizable(container: EventTarget, target: Element)(onWidthChanged: Double => Unit):
  target.addEventListener("mousedown", onDown)
  container.addEventListener("mousemove", onMove)
  container.addEventListener("mouseup", onUp)

  def domElement: Element = target

  private case class Clicked(cx: Double, width: Double)
  private var clicked: Clicked = null
  private var onEdge: Boolean = false

  private def onDown(e: MouseEvent): Unit =
    val width = target.getBoundingClientRect.width
    if onEdge then
      clicked = Clicked(e.clientX, width)

  private def onMove(e: MouseEvent): Unit =
    val boundingRect = target.getBoundingClientRect
    val x = e.clientX - boundingRect.left
    
    if Math.abs(x) < 4 then
      onEdge = true
      target.style.cursor = "ew-resize"
    else
      onEdge = false
      target.style.cursor = "default"
    
    if clicked != null then
      val width = clicked.cx - e.clientX + clicked.width
      onWidthChanged(width)

  private def onUp(e: MouseEvent): Unit =
    clicked = null

extension (target: Element)
  def leftResizableIn(container: EventTarget)(onWidthChanged: Double => Unit): Element =
    val resizable = new LeftResizable(container, target)(onWidthChanged)
    resizable.domElement
