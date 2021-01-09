package astone

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import scala.scalajs.js.typedarray.Int8Array

import org.scalajs.dom._
import org.scalajs.dom.html._

import scalatags.JsDom.all._

import facade.three._
import facade.pico
import facade.web._

import astone.model._
import astone.scene._

class DetectionMonitor(viewWidth: Int, viewHeight: Int, webcamSettings: WebcamSettings):
  // width refer to style width, so we use widthA instead 
  val camView = canvas(id := "cam-view", widthA := webcamSettings.width, heightA := webcamSettings.height).render
  val vCamView = canvas(id := "vcam-view", widthA := viewWidth, heightA := viewHeight).render
  val frontView = canvas(id := "front-view", widthA := viewWidth, heightA := viewHeight).render
  val topView = canvas(id := "top-view", widthA := viewWidth, heightA := viewHeight).render
  val domElement = div(id := "detection")(div(camView), div(vCamView), div(frontView), div(topView)).render

  val vCam = WebGLRenderer(literal(canvas = vCamView))
  val front = WebGLRenderer(literal(canvas = frontView))
  val top = WebGLRenderer(literal(canvas = topView))

  val topCamera = OrthographicCamera(
    -1.5 * webcamSettings.width, 1.5 * webcamSettings.width,
    1.5 * 240, -1.5 * 240, 0, 3d * webcamSettings.focal
    )
  topCamera.position.y = 1.5 * webcamSettings.height
  topCamera.position.z = 1.5 * 240
  topCamera.lookAt(0d, 0d, 1.5 * 240)

  val frontCamera = OrthographicCamera(
    -1.5 * webcamSettings.width, 1.5 * webcamSettings.width,
    1.5 * 240, -1.5 * 240, 0, 6d * webcamSettings.focal
  )
  frontCamera.position.y = 0.5 * webcamSettings.height 
  frontCamera.position.z = 5d * webcamSettings.focal
  frontCamera.lookAt(0, 0.5 * webcamSettings.height, 0)

  val ctx = camView.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  def drawImage(video: Video): js.Array[Int] =
    ctx.drawImage(video, 0, 0, webcamSettings.width, webcamSettings.height)
    ctx.getImageData(0, 0, webcamSettings.width, webcamSettings.height).data

  def onDetection(face: FaceDetection, scene: VirtualReality): Unit =
    val (x, y, scale) = (face.x, face.y, face.scale)
    ctx.beginPath()
    ctx.arc(x, y, 0.5 * scale, 0, 2 * Math.PI, false)
    ctx.lineWidth = 3
    ctx.strokeStyle = "red"
    ctx.stroke()

    vCam.render(scene, scene.webcam)
    front.render(scene, frontCamera)
    top.render(scene, topCamera)

    