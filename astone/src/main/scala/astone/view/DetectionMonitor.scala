package astone

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

import org.scalajs.dom._
import org.scalajs.dom.html.{Video, Div, Canvas}

import scalatags.JsDom._
import scalatags.JsDom.all._

import facade.three._

import astone.model._
import astone.scene.VirtualReality

import astone.view.component._

class DetectionMonitor(
  width: Int,
  webcamSettings: WebcamSettings,
  windowSettings: WindowSettings,
  scene: VirtualReality,
  video: Video
) extends Component[Div]:
  var sizeChanged = true
  var viewWidth = width
  var ratio = viewWidth.toDouble / webcamSettings.width
  var viewHeight = webcamSettings.height * ratio
  
  var face: FaceDetection = null
  var faceMoved = true

  val camView = section("Webcam", viewWidth, viewHeight)
  val vCamView = section("Virtual Webcam", viewWidth, viewHeight)
  val frontView = section("Virtual Front View", viewWidth, viewHeight)
  val topView = section("Virtual Top View", viewWidth, viewHeight)

  def container = document.body
  val element: Div = div(
    marginRight:= "15px",
    position := "absolute",
    top := 0,
    right := 0,
    zIndex := 100,
    background := 0x1a1a1a,
    color := 0xeee,
    padding := "3px",
    borderLeft := "2px solid purple"
  )(camView.element, vCamView.element, frontView.element, topView.element)
    .render

  val vCam = WebGLRenderer(literal(canvas = vCamView.canvas.element))
  val front = WebGLRenderer(literal(canvas = frontView.canvas.element))
  val topRenderer = WebGLRenderer(literal(canvas = topView.canvas.element))

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

  def render(): Unit =
    if sizeChanged then
      camView.setSize(viewWidth, viewHeight)
      vCamView.setSize(viewWidth, viewHeight)
      frontView.setSize(viewWidth, viewHeight)
      topView.setSize(viewWidth, viewHeight)
      
      vCam.setSize(viewWidth, viewHeight)
      front.setSize(viewWidth, viewHeight)
      topRenderer.setSize(viewWidth, viewHeight)

    camView.context2D.drawImage(video, 0, 0, viewWidth, viewHeight)
    
    if faceMoved || sizeChanged then
      if face != null then
        val (x, y, scale) = (face.x * ratio, face.y * ratio, face.scale * ratio)
        camView.context2D.beginPath()
        camView.context2D.arc(x, y, 0.5 * scale, 0, 2 * Math.PI, false)
        camView.context2D.lineWidth = 3
        camView.context2D.strokeStyle = "red"
        camView.context2D.stroke()
    
      vCam.render(scene, scene.webcam)
      front.render(scene, frontCamera)
      topRenderer.render(scene, topCamera)
    
    faceMoved = false
    sizeChanged = false

  def onDetection(detection: FaceDetection): Unit =
    if (face != detection)
      face = detection
      faceMoved = true
    
  def onWidthChanged(width: Double): Unit =
    viewWidth = width.toInt
    ratio = width / webcamSettings.width
    viewHeight = webcamSettings.height * ratio
    sizeChanged = true

  def section(title: String, width: Double, height: Double): MonitorSection =
    val section = new MonitorSection(title)
    section.setSize(width, height)
    section

class MonitorSection(title: String) extends Component[Div]:
  val canvas: Collapsible[Canvas] = tags.canvas(display := "block").render.collapsible
  val button = tags.button(display := "block")(title).render
  button.onclick = (_ => canvas.collapse())
  
  def element = div(button, canvas.element).render

  def context2D: CanvasRenderingContext2D =
    canvas.element.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  def setSize(width: Double, height: Double): Unit =
    canvas.element.width = width.toInt
    canvas.element.height = height.toInt
    canvas.setSize(width, height)
    button.style.width = s"${width.toInt}px"
  