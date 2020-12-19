package astone

import org.scalajs.dom.{document, Event, MouseEvent, Node}

@main def app(): Unit =
  document.addEventListener[Event](
    "DOMContentLoaded",
    _ => setupUI()
  )

private def setupUI(): Unit =
  document.body.appendPar("Hello World")

  val button = document.createElement("button")
  button.textContent = "Click me!"
  button.addEventListener[MouseEvent](
    "click", 
    _ => document.body.appendPar("You clicked the button!")
  )
  document.body.appendChild(button)

extension (target: Node)
  def appendPar(text: String): Unit = 
    val par = document.createElement("p")
    par.textContent = text
    target.appendChild(par)
