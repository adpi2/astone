package astone

import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel

object App {
  

  def main(args: Array[String]): Unit = {
    document.addEventListener[dom.Event](
      "DOMContentLoaded",
      _ => setupUI()
    )
  }

  def setupUI(): Unit = {
    appendPar(document.body, "Hello World")

    val button = document.createElement("button")
    button.textContent = "Click me!"
    button.addEventListener[dom.MouseEvent](
      "click", 
      _ => appendPar(document.body, "You clicked the button!")
    )
    document.body.appendChild(button)
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }
}