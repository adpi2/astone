package astone.scene

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model.WindowSettings

@JSImport("./Tis-the-Season.jpg", JSImport.Default)
@js.native
val tisTheSeason: String = js.native

// @JSImport("three/examples/fonts/helvetiker_bold.typeface.json", JSImport.Default)
// @js.native
// val helvetiker: js.Object = js.native

class DemoScene(windowSettings: WindowSettings) extends facade.three.Scene:
  val texture = TextureLoader().load(tisTheSeason)
  texture.wrapS = RepeatWrapping
  texture.wrapT = RepeatWrapping
  
  val cube =
    val geometry = BoxGeometry(0.3 * windowSettings.height, 0.3 * windowSettings.height, 0.3 * windowSettings.height)
    val material = MeshStandardMaterial(literal(map = texture))
    new Mesh(geometry, material)

  cube.position.z -= 0.15 * windowSettings.height
  
  val hemiLight = HemisphereLight(0xffffdd, 0x000000, 0.6)
  hemiLight.lookAt(windowSettings.height, 0, 0)

  val windowLight = DirectionalLight(0xddddff, 0.7)
  windowLight.position.z = windowSettings.height
  windowLight.position.x = -windowSettings.height

  add(cube)
  add(hemiLight)
  add(windowLight)
  
  def nextFrame(): Unit =
    cube.rotation.y += 0.1
  
  