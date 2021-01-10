package astone

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

import org.scalajs.dom._
import org.scalajs.dom.html.Video

import scalatags.JsDom.all._

import facade.three._

import astone.model._
import astone.scene.VirtualReality

class DetectionMonitor(viewWidth: Int, webcamSettings: WebcamSettings, windowSettings: WindowSettings):
  val viewHeight = webcamSettings.height * viewWidth / webcamSettings.width

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
    left = -1.5 * windowSettings.width,
    right = 1.5 * windowSettings.width,
    top = 1.5 * webcamSettings.height * windowSettings.width / webcamSettings.width,
    bottom = -1.5 * webcamSettings.height * windowSettings.width / webcamSettings.width,
    near = 0,
    far = 3 * windowSettings.height
  )
  topCamera.position.y = 1.5 * windowSettings.height
  topCamera.position.z = 1.5 * webcamSettings.height * windowSettings.width / webcamSettings.width
  topCamera.lookAt(0d, 0d, 1.5 * webcamSettings.height * windowSettings.width / webcamSettings.width)

  val frontCamera = OrthographicCamera(
    left = -1.5 * windowSettings.width,
    right = 1.5 * windowSettings.width,
    top = 1.5 * webcamSettings.height * windowSettings.width / webcamSettings.width,
    bottom = -1.5 * webcamSettings.height * windowSettings.width / webcamSettings.width,
    near = 0,
    far = 3 * windowSettings.width * webcamSettings.focal / webcamSettings.width
  )
  frontCamera.position.z = 3 * windowSettings.width * webcamSettings.focal / webcamSettings.width
  frontCamera.lookAt(0, 0, 0)

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

    