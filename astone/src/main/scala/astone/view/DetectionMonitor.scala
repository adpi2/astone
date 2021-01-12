package astone

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

import org.scalajs.dom._
import org.scalajs.dom.html.{Video, Div}

import scalatags.JsDom.all._

import facade.three._

import astone.model._
import astone.scene.VirtualReality

import astone.view.component._

class DetectionMonitor(width: Int, webcamSettings: WebcamSettings, windowSettings: WindowSettings, scene: VirtualReality, video: Video):
  var viewWidth = width
  var ratio = viewWidth.toDouble / webcamSettings.width
  var viewHeight = webcamSettings.height * ratio
  
  var face: FaceDetection = null

  // width refer to style width, so we use widthA instead 
  val camView = canvas(widthA := viewWidth, heightA := viewHeight, display := "block").render
  val vCamView = canvas(widthA := viewWidth, heightA := viewHeight, display := "block").render
  val frontView = canvas(widthA := viewWidth, heightA := viewHeight, display := "block").render
  val topView = canvas(widthA := viewWidth, heightA := viewHeight, display := "block").render
  val domElement = div(
    marginRight:= "15px",
    position := "absolute",
    top := 0,
    right := 0,
    zIndex := 100,
    background := 0x1a1a1a,
    color := 0xeee,
    padding := "3px",
    borderLeft := "2px solid purple"
  )(camView, vCamView, frontView, topView)
    .render
    .leftResizableIn(window)(onWidthChanged)

  val vCam = WebGLRenderer(literal(canvas = vCamView))
  val front = WebGLRenderer(literal(canvas = frontView))
  val topRenderer = WebGLRenderer(literal(canvas = topView))

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

  private def animate(): Unit =
    camView.width = viewWidth
    camView.height = viewHeight.toInt
    vCam.setSize(viewWidth, viewHeight)
    front.setSize(viewWidth, viewHeight)
    topRenderer.setSize(viewWidth, viewHeight)

    ctx.drawImage(video, 0, 0, viewWidth, viewHeight)
    
    if face !=null then
      val (x, y, scale) = (face.x * ratio, face.y * ratio, face.scale * ratio)
      ctx.beginPath()
      ctx.arc(x, y, 0.5 * scale, 0, 2 * Math.PI, false)
      ctx.lineWidth = 3
      ctx.strokeStyle = "red"
      ctx.stroke()

    vCam.render(scene, scene.webcam)
    front.render(scene, frontCamera)
    topRenderer.render(scene, topCamera)

  def onDetection(detection: FaceDetection): Unit =
    face = detection
    window.requestAnimationFrame(_ => animate())
    
  def onWidthChanged(width: Double): Unit =
    viewWidth = width.toInt
    ratio = width / webcamSettings.width
    viewHeight = webcamSettings.height * ratio
    window.requestAnimationFrame(_ => animate())
