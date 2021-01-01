package astone

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import org.scalajs.dom.CanvasRenderingContext2D

import facade.pico

import astone.model.FaceDetection

@JSImport("./facefinder.cascade", JSImport.Default)
@js.native
val facefinderUrl: String = js.native

class FaceDetector(cascade: pico.Cascade, height: Int, width: Int):
  private val updateMemory = pico.instantiate_detection_memory(1)

  private val params = pico.Params(
    shiftfactor = 0.1,
    minsize = 30,
    maxsize = 1000,
    scalefactor = 1.1
  )

  def detect(rgba: js.Array[Int]): Option[FaceDetection] =
    val grayScale = rgbaToGrayscale(rgba)
    val image = pico.Image(grayScale, height, width, ldim = width)
    val detections = pico.run_cascade(image, cascade, params)
    val updated = updateMemory(detections)
    val faces = pico.cluster_detections(updated, 0.2)
    for face <- faces.sortBy(face => -face(3)).headOption
    yield
      FaceDetection(face(1), face(0), face(2))

  private def rgbaToGrayscale(rgba: js.Array[Int]): js.Array[Int] =
    val gray = new js.Array[Int](height * width)
    for
      row <- 0 until height
      col <- 0 until width
    do 
      val idx = row * width + col
      val index = row * 4 * width + 4 * col
      val value = (2 * rgba(4 * idx) + 7 * rgba(4 * idx + 1) + rgba(4 * idx + 2)) / 10
      gray.update(idx, value.toShort)
    gray

