package facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.raw.HTMLElement

@JSImport("./camvas.js", JSImport.Namespace)
@js.native
object camvas extends js.Object:
  def camvas(ctx: CanvasRenderingContext2D, draw: js.Function2[HTMLElement, js.Object, Unit]): js.Object = js.native
