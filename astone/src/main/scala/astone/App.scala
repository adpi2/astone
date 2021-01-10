package astone

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.scalajs.js.typedarray.Int8Array

import org.scalajs.dom._
import org.scalajs.dom.raw.{HTMLCanvasElement, HTMLVideoElement, Node}
import org.scalajs.dom.experimental.Fetch
import org.scalajs.dom.experimental.webrtc._
import org.scalajs.dom.experimental.mediastream.MediaStreamConstraints

import scalatags.JsDom.all._

import facade.three._
import facade.pico
import facade.web._

import astone.model._
import astone.scene._

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener[Event](
      "DOMContentLoaded",
      _ => 
        try
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
      document.body.appendChild(screenView.domElement)
    
      val video = document.createElement("video").asInstanceOf[HTMLVideoElement]
      video.setAttribute("autoplay", "1")
      video.setAttribute("style", "display:none")
      document.body.appendChild(video)

      val mediaConstraints = MediaStreamConstraints(video = true)
      for stream <- window.navigator.mediaDevices.getUserMedia(mediaConstraints).toFuture
      do video.srcObject = stream

      video.onloadedmetadata = _ => onCamLoaded(video, cascade, screenView)  
  
  private def onCamLoaded(video: HTMLVideoElement, cascade: pico.Cascade, screenView: WebGLRenderer): Unit =    
    val screenWidth = 1920
    val screenWidth_mm = 345.6
    val headSize_mm = 175
    val headSize = headSize_mm * screenWidth / screenWidth_mm

    val webcamSettings = WebcamSettings(650, diagViewAngle = 78, video.videoWidth, video.videoHeight)
    val windowSettings = WindowSettings(window.innerWidth, window.innerHeight)

    val monitor = DetectionMonitor(320, webcamSettings, windowSettings)
    document.body.appendChild(monitor.domElement)

    val demoScene = DemoScene(windowSettings)
    val scene = VirtualReality(webcamSettings, windowSettings, headSize)

    val detector = FaceDetector(cascade, webcamSettings)

    def loop(): Unit =
      val rgba = monitor.drawImage(video)
      for detection <- detector.detect(rgba)
      do
        scene.onHeadMoved(
          x = (0.5 * webcamSettings.width - detection.x) * headSize / detection.scale,
          y = (0.5 * webcamSettings.height- detection.y) * headSize / detection.scale + webcamSettings.y,
          z = webcamSettings.focal * headSize / detection.scale
        )
        monitor.onDetection(detection, scene)
      
      screenView.render(demoScene, scene.headCam)
      window.requestAnimationFrame(_ => loop())

    window.requestAnimationFrame(_ => loop())

