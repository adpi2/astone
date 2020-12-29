package astone

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Dynamic.literal
import facade.three._

@JSImport("./Tis-the-Season.jpg", JSImport.Default)
@js.native
val tisTheSeason: String = js.native

@JSImport("three/examples/fonts/helvetiker_bold.typeface.json", JSImport.Default)
@js.native
val helvetiker: js.Object = js.native

object DemoScene extends facade.three.Scene:
  val texture = TextureLoader().load(tisTheSeason)
  texture.wrapS = RepeatWrapping
  texture.wrapT = RepeatWrapping
  
  val cube =
    val geometry = BoxGeometry(0.3, 0.3, 0.3)
    val material = MeshBasicMaterial(literal(map = texture))
    new Mesh(geometry, material)

  cube.position.y += 1

  val line =
    val points = js.Array(
      new Vector3(-1.5, 0, 0),
      new Vector3(0, 1.5, 0),
      new Vector3(1.5, 0, 0)      
    )
    val geometry = BufferGeometry()
    geometry.setFromPoints(points)
    val material = LineBasicMaterial(literal(map = 0x0000ff))
    new Line(geometry, material)

  val title =
    val font = Font(helvetiker)
    val parameters = literal(
      font = font,
      size = 0.5,
      height = 0.2
    )
    val geometry = TextGeometry("Hello!", parameters)
    val material = MeshBasicMaterial(literal(map = texture))
    new Mesh(geometry, material)
    
  title.position.x -= 0.75

  add(title)
  add(cube)
  add(line)
  
  def nextFrame(): Unit =
    line.rotation.y += 0.02
    cube.rotation.x += 0.002
    cube.rotation.y += 0.01
    title.rotation.y += 0.001
  
  