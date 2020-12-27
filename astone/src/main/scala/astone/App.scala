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
          setupFaceDetection()
          setupScene()
        catch e => println(e)
    )

  private def setupFaceDetection(): Unit =
    for 
      response <- Fetch.fetch(facefinderUrl).toFuture
      buffer <- response.arrayBuffer.toFuture
    do
      val bytes = new Int8Array(buffer)
      val cascade = pico.unpack_cascade(bytes)

      val canvas = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
      document.body.appendChild(canvas)

      val button = document.createElement("button")
      button.textContent = "Start Detection"
      button.addEventListener[MouseEvent]("click", e => startDetection(canvas, cascade))
      document.body.appendChild(button)

  private def startDetection(canvas: HTMLCanvasElement, cascade: pico.Cascade): Unit =
    val video = document.createElement("video").asInstanceOf[HTMLVideoElement]
    video.setAttribute("autoplay", "1")
    video.setAttribute("style", "display:none")
    document.body.appendChild(video)
    
    val faceDetection = FaceDetection(canvas, cascade)

    val mediaConstraints = MediaStreamConstraints(video = true)
    for
      stream <- window.navigator.mediaDevices.getUserMedia(mediaConstraints).toFuture
    do
      video.srcObject = stream
      video.onloadedmetadata = _ =>
        canvas.width = video.videoWidth
        canvas.height = video.videoHeight

      def loop(): Unit =
        faceDetection.draw(video)
        window.requestAnimationFrame(_ => loop())

      window.requestAnimationFrame(_ => loop())

  private def setupScene(): Unit =
    val camera = new PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 100)
    val renderer = new WebGLRenderer()
    renderer.setSize(window.innerWidth / 2, window.innerHeight / 2)

    camera.position.z = 2

    def animate(): Unit =
      window.requestAnimationFrame(_ => animate())
      Scene.nextFrame()
      renderer.render(Scene, camera)

    animate()
    
    document.body.appendChild(renderer.domElement)
