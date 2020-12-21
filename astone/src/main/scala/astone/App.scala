package astone

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import scalajs.three._
import org.scalajs.dom._
import scala.scalajs.js.annotation.JSImport

@JSImport("./Tis-the-Season.jpg", JSImport.Default)
@js.native
val tisTheSeason: String = js.native

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener[Event](
      "DOMContentLoaded",
      _ => 
        setupScene()
    )

  private def setupScene(): Unit =
    val scene = new Scene()
    val camera = new PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 100)
    val renderer = new WebGLRenderer()
    renderer.setSize(window.innerWidth, window.innerHeight)

    val cube = createCube()
    val line = createLine()
    scene.add(cube)

    camera.position.z = 2

    def animate(): Unit =
      window.requestAnimationFrame(_ => animate())
      
      cube.rotation.x += 0.01
      cube.rotation.y += 0.01

      renderer.render(scene, camera)

    animate()

    document.body.appendChild(renderer.domElement)

  private def createCube(): Mesh =
    val texture = new TextureLoader().load(tisTheSeason)
    texture.wrapS = RepeatWrapping
    texture.wrapT = RepeatWrapping
    val geometry = new BoxGeometry()
    val material = new MeshBasicMaterial(literal(map = texture))
    new Mesh(geometry, material)

  private def createLine(): Line =
    val points = js.Array(
      new Vector3(-2, 0, 0),
      new Vector3(0, 2, 0),
      new Vector3(2, 0, 0)      
    )
    val geometry = new BufferGeometry()
    geometry.setFromPoints(points)
    val material = new LineBasicMaterial(literal(color = 0x0000ff))
    new Line(geometry, material)

