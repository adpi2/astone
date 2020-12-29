package astone

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.scalajs.js.typedarray.Int8Array

import org.scalajs.dom._
import org.scalajs.dom.raw.{HTMLCanvasElement, HTMLVideoElement}
import org.scalajs.dom.experimental.Fetch
import org.scalajs.dom.experimental.webrtc._
import org.scalajs.dom.experimental.mediastream.MediaStreamConstraints

import facade.three.{PerspectiveCamera, WebGLRenderer}
import facade.pico
import facade.web._

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener[Event](
      "DOMContentLoaded",
      _ => 
        try
          setupDemoScene()
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

      val detection = document.createElement("div")
      detection.setAttribute("id", "detection")
      document.body.appendChild(detection)

      val canvasDiv = document.createElement("div")
      val canvas = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
      canvasDiv.appendChild(canvas)
      detection.appendChild(canvasDiv)

      val buttonDiv = document.createElement("div")
      val button = document.createElement("button")
      button.setAttribute("style", "float: right")
      button.textContent = "Start Detection"
      button.addEventListener[MouseEvent]("click", e => startDetection(canvas, cascade))
      buttonDiv.appendChild(button)
      detection.appendChild(buttonDiv)

  private def startDetection(canvas: HTMLCanvasElement, cascade: pico.Cascade): Unit =
    val video = document.createElement("video").asInstanceOf[HTMLVideoElement]
    video.setAttribute("autoplay", "1")
    video.setAttribute("style", "display:none")
    document.body.appendChild(video)

    val mediaConstraints = MediaStreamConstraints(video = true)
    for
      stream <- window.navigator.mediaDevices.getUserMedia(mediaConstraints).toFuture
    do
      video.srcObject = stream
      video.onloadedmetadata = _ =>
        val width = 320
        val height = (width * video.videoHeight) / video.videoWidth

        canvas.width = width
        canvas.height = height

        val ctx = canvas.getContext("2d")
          .asInstanceOf[CanvasRenderingContext2D]
        val faceDetection = FaceDetection(ctx, cascade)

        def loop(): Unit =
          ctx.drawImage(video, 0D, 0D, width, height)
          faceDetection.draw()
          window.requestAnimationFrame(_ => loop())

        window.requestAnimationFrame(_ => loop())

  private def setupDemoScene(): Unit =
    val camera = new PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 100)
    val renderer = new WebGLRenderer()
    renderer.setSize(window.innerWidth, window.innerHeight)

    camera.position.z = 2

    def animate(): Unit =
      window.requestAnimationFrame(_ => animate())
      DemoScene.nextFrame()
      renderer.render(DemoScene, camera)

    animate()
    
    document.body.appendChild(renderer.domElement)
