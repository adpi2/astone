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

class DemoScene(screenWidth: Double, screenHeight: Double) extends facade.three.Scene:
  val texture = TextureLoader().load(tisTheSeason)
  texture.wrapS = RepeatWrapping
  texture.wrapT = RepeatWrapping
  
  val cube =
    val geometry = BoxGeometry(0.3 * screenHeight, 0.3 * screenHeight, 0.3 * screenHeight)
    val material = MeshBasicMaterial(literal(map = texture))
    new Mesh(geometry, material)

  // cube.position.z += -1d * screenHeight

  val title =
    val font = Font(helvetiker)
    val parameters = literal(
      font = font,
      size = 0.1 * screenWidth,
      height = 0.05 * screenWidth
    )
    val geometry = TextGeometry("Hello!", parameters)
    val material = MeshBasicMaterial(literal(map = texture))
    new Mesh(geometry, material)
    
  title.position.x -= 0.1 * screenWidth 

  // add(title)
  add(cube)
  
  def nextFrame(): Unit =
    cube.rotation.y += 0.1
    title.rotation.y += 0.001
  
  