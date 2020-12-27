package astone

import org.scalajs.dom._

import facade.three.{PerspectiveCamera, WebGLRenderer}

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener[Event](
      "DOMContentLoaded",
      _ => 
        try 
          FaceDetection.setup()
        catch e => println(e)
    )

  private def setupScene(): Unit =
    val camera = new PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 100)
    val renderer = new WebGLRenderer()
    renderer.setSize(window.innerWidth, window.innerHeight)

    camera.position.z = 2

    def animate(): Unit =
      window.requestAnimationFrame(_ => animate())
      Scene.nextFrame()
      renderer.render(Scene, camera)

    animate()
    
    document.body.appendChild(renderer.domElement)
