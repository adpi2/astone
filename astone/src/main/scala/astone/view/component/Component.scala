package astone.view.component

import org.scalajs.dom.html.Element

trait Component[+E <: Element]:
  def element: E

object Component:
  def apply[E <: Element](e: E) = new Component[E]:
    def element: E = e
