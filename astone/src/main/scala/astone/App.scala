package astone

import scala.concurrent.ExecutionContext.Implicits.global

import scala.scalajs.js.typedarray.Int8Array

import org.scalajs.dom._
import org.scalajs.dom.html._
import org.scalajs.dom.experimental.Fetch
import org.scalajs.dom.experimental.webrtc._
import org.scalajs.dom.experimental.mediastream.MediaStreamConstraints

import scalatags.JsDom.all._

import facade.three._
import facade.pico

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

      val mediaConstraints = MediaStreamConstraints(video = true)
      for stream <- window.navigator.mediaDevices.getUserMedia(mediaConstraints).toFuture
      do 
        val videoElement = video(
          attr("autoplay") := 1,
          display := "none",
          attr("srcObject") := stream
        ).render
        // document.body.appendChild(videoElement)
        videoElement.onloadedmetadata = _ => onCamLoaded(videoElement, cascade, screenView)
  
  private def onCamLoaded(video: Video, cascade: pico.Cascade, screenView: WebGLRenderer): Unit =
    val screenWidth = 1920
    val screenWidth_mm = 345.6
    val headSize_mm = 175
    val headSize = headSize_mm * screenWidth / screenWidth_mm

    val webcamSettings = WebcamSettings(650, diagViewAngle = 78, video.videoWidth, video.videoHeight)
    val windowSettings = WindowSettings(window.innerWidth, window.innerHeight)

    val canvasCtx = 
      canvas(widthA := webcamSettings.width, heightA := webcamSettings.height, display := "none")
        .render
        .getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    

    val demoScene = DemoScene(windowSettings)
    val scene = VirtualReality(webcamSettings, windowSettings, headSize)

    val monitor = DetectionMonitor(320, webcamSettings, windowSettings, scene, video)
    document.body.appendChild(monitor.domElement)

    val detector = FaceDetector(cascade, webcamSettings)

    def loop(): Unit =
      canvasCtx.drawImage(video, 0, 0, webcamSettings.width, webcamSettings.height)
      val rgba = canvasCtx.getImageData(0, 0, webcamSettings.width, webcamSettings.height).data
      detector.detect(rgba) match
        case None => monitor.onDetection(null)
        case Some(face) =>
          scene.onHeadMoved(
            x = (0.5 * webcamSettings.width - face.x) * headSize / face.scale,
            y = (0.5 * webcamSettings.height- face.y) * headSize / face.scale + webcamSettings.y,
            z = webcamSettings.focal * headSize / face.scale
          )
          monitor.onDetection(face)
      
      screenView.render(demoScene, scene.headCam)
      window.requestAnimationFrame(_ => loop())
    
    window.requestAnimationFrame(_ => loop())

