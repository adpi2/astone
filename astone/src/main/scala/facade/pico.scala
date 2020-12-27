package facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.typedarray.Int8Array


object pico:
  @JSImport("./pico.js", "instantiate_detection_memory")
  @js.native
  def instantiate_detection_memory(size: Int): UpdateMemory = js.native

  @JSImport("./pico.js", "unpack_cascade")
  @js.native
  def unpack_cascade(bytes: Int8Array): Cascade = js.native

  @JSImport("./pico.js", "run_cascade")
  @js.native
  def run_cascade(image: Image, cascade: Cascade, params: Params): js.Array[Detection] = js.native

  @JSImport("./pico.js", "cluster_detections")
  @js.native
  def cluster_detections(detections: js.Array[Detection], threshold: Double): js.Array[Detection] = js.native

  trait Cascade extends js.Object

  type UpdateMemory = js.Function1[js.Array[Detection], js.Array[Detection]]
  type Detection = js.Tuple4[Double, Double, Double, Double]

  class Image(val pixels: js.Array[Int], val nrows: Int, val ncols: Int, val ldim: Int) extends js.Object
  class Params(val shiftfactor: Double, val minsize: Int, val maxsize: Int, val scalefactor: Double) extends js.Object
  