package astone

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.scalajs.js.typedarray.Int8Array

import org.scalajs.dom._
import org.scalajs.dom.raw.{HTMLCanvasElement, HTMLVideoElement}
import org.scalajs.dom.experimental.Fetch
import org.scalajs.dom.experimental.webrtc._
import org.scalajs.dom.experimental.mediastream.MediaStreamConstraints

import facade.three._
import facade.pico
import facade.web._

import astone.model._

object App:
  private val defaultWidth = 320
  private val defaultHeight = 240

  def main(args: Array[String]): Unit =
    document.addEventListener[Event](
      "DOMContentLoaded",
      _ => 
        try
          // setupDemoScene()
          setupFaceDetection()
        catch e => println(e)
    )

  private def setupFaceDetection(): Unit =
    for 
      response <- Fetch.fetch(facefinderUrl).toFuture
      buffer <- response.arrayBuffer.toFuture
    do
      val bytes = new Int8Array(buffer)
      val cascade = pico.unpack_cascade(bytes)

      val screenView = new WebGLRenderer()
      screenView.setSize(window.innerWidth, window.innerHeight)

      val detection = document.createElement("div")
      detection.setAttribute("id", "detection")
      
      val cam = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
      cam.width = defaultWidth
      cam.height = defaultHeight
      val topView = WebGLRenderer()
      topView.setSize(defaultWidth, defaultHeight)
      val frontView = WebGLRenderer()
      frontView.setSize(defaultWidth, defaultHeight)
      
      val buttonDiv = document.createElement("div")
      val button = document.createElement("button")
      button.setAttribute("style", "float: right")
      button.textContent = "Start Detection"
      button.addEventListener[MouseEvent]("click", e => startDetection(cam, cascade, frontView, topView, screenView))

      buttonDiv.appendChild(button)
      detection.appendChild(buttonDiv)
      detection.appendChild(cam)
      detection.appendChild(frontView.domElement)   
      detection.appendChild(topView.domElement)
      document.body.appendChild(screenView.domElement)
      document.body.appendChild(detection)
      

  private def startDetection(cam: HTMLCanvasElement, cascade: pico.Cascade, frontView: WebGLRenderer, topView: WebGLRenderer, screenView: WebGLRenderer): Unit =
    val video = document.createElement("video").asInstanceOf[HTMLVideoElement]
    video.setAttribute("autoplay", "1")
    video.setAttribute("style", "display:none")
    document.body.appendChild(video)

    val mediaConstraints = MediaStreamConstraints(video = true)
    for stream <- window.navigator.mediaDevices.getUserMedia(mediaConstraints).toFuture
    do video.srcObject = stream
    
    video.onloadedmetadata = _ => onCamLoaded(video, cam, cascade, frontView, topView, screenView)
  
  private def onCamLoaded(video: HTMLVideoElement, cam: HTMLCanvasElement, cascade: pico.Cascade, frontView: WebGLRenderer, topView: WebGLRenderer, screenView: WebGLRenderer): Unit =
    val width = defaultWidth
    val height = (width * video.videoHeight) / video.videoWidth
    cam.width = width
    cam.height = height

    val width_mm = 345.6
    // val heigth_mm = 194.4
    val head_mm = 180d
    val headSize = head_mm * width / width_mm
    val diagViewAngle = Math.toRadians(78D)
    
    val diag = Math.sqrt(width * width + height * height)
    val focal = diag / (2D * Math.tan(diagViewAngle / 2D))

    val screenWidth = width
    val screenHeight = window.innerHeight * width / window.innerWidth

    val webcam = Webcam(110d, focal, width, height)

    val demoScene = DemoScene(screenWidth, screenHeight)
    val scene = RealScene(webcam, screenWidth, screenHeight, headSize)
    val topCamera = OrthographicCamera(-1.5 * width, 1.5 * width, 1.5 * defaultHeight, -1.5 * defaultHeight, 0d, 3d * focal)
    topCamera.position.y = 1.5 * height
    topCamera.position.z = 1.5 * defaultHeight
    topCamera.lookAt(0d, 0d, 1.5 * defaultHeight)

    val frontCamera = OrthographicCamera(-1.5 * width, 1.5 * width, 1.5 * defaultHeight, -1.5 * defaultHeight, 0d, 6d * focal)
    frontCamera.position.y = 0.5 * height 
    frontCamera.position.z = 5d * focal
    frontCamera.lookAt(0d, 0.5 * height, 0d)

    val ctx = cam.getContext("2d")
      .asInstanceOf[CanvasRenderingContext2D]
    val detector = FaceDetector(cascade, height, width)

    def loop(): Unit =
      ctx.drawImage(video, 0D, 0D, width, height)
      val rgba = ctx.getImageData(0D, 0D, width, height).data
      for detection <- detector.detect(rgba)
      do 
        scene.computeHeadPosition(detection)
        drawDetection(ctx, detection)
      frontView.render(scene, frontCamera)
      topView.render(scene, topCamera)
      // demoScene.nextFrame()
      screenView.render(demoScene, scene.headCam)
      window.requestAnimationFrame(_ => loop())

    window.requestAnimationFrame(_ => loop())

  private def drawDetection(ctx: CanvasRenderingContext2D, face: FaceDetection): Unit =
    val (x, y, scale) = (face.x, face.y, face.scale)
    ctx.beginPath()
    ctx.arc(x, y, 0.5 * scale, 0d, 2d * Math.PI, false)
    ctx.lineWidth = 3d
    ctx.strokeStyle = "red"
    ctx.stroke()

