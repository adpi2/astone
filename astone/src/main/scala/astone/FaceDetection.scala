package astone

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.typedarray.Int8Array

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.raw.{HTMLElement, HTMLCanvasElement}
import org.scalajs.dom.experimental.Fetch

import facade.pico

@JSImport("./facefinder.cascade", JSImport.Default)
@js.native
val facefinderUrl: String = js.native

class FaceDetection(canvas: HTMLCanvasElement, cascade: pico.Cascade):
  private val updateMemory = pico.instantiate_detection_memory(5)

  private val ctx: CanvasRenderingContext2D = 
    canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  def draw(video: HTMLElement): Unit =
    ctx.drawImage(video, 0D, 0D)
    val rgba = ctx.getImageData(0D, 0D, canvas.width, canvas.height).data
    val image = pico.Image(
      pixels= rgbaToGrayscale(rgba, nrows = canvas.height, ncols = canvas.width),
      nrows = canvas.height,
      ncols = canvas.width,
      ldim = canvas.width
    )
    val params = pico.Params(
      shiftfactor = 0.1,
      minsize = 50,
      maxsize = 1000,
      scalefactor = 1.1
    )
    val detections = pico.run_cascade(image, cascade, params)
    val updated = updateMemory(detections)
    val faces = pico.cluster_detections(updated, 0.2)
    for face <- faces if face(3) > 50D do draw(face)

  private def draw(face: pico.Detection): Unit =
    try
      val (row, col, scale, _) = face: (Double, Double, Double, Double)
      ctx.beginPath()
      ctx.arc(col, row, scale / 2, 0, 2 * Math.PI, false)
      ctx.lineWidth = 10
      ctx.strokeStyle = "red"
      ctx.stroke()
    catch e => println(e)
    

  private def rgbaToGrayscale(rgba: js.Array[Int], nrows: Int, ncols: Int): js.Array[Int] =
    val gray = new js.Array[Int](nrows * ncols)
    for
      row <- 0 until nrows
      col <- 0 until ncols
    do 
      val idx = row * ncols + col
      val index = row * 4 * ncols + 4 * col
      val value = (2 * rgba(4 * idx) + 7 * rgba(4 * idx + 1) + rgba(4 * idx + 2)) / 10
      gray.update(idx, value.toShort)
    gray

