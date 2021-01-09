package astone.scene

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model._

@JSImport("./Tis-the-Season.jpg", JSImport.Default)
@js.native
val tisTheSeason: String = js.native

// @JSImport("three/examples/fonts/helvetiker_bold.typeface.json", JSImport.Default)
// @js.native
// val helvetiker: js.Object = js.native

class DemoScene(screenSettings: ScreenSettings) extends facade.three.Scene:
  val texture = TextureLoader().load(tisTheSeason)
  texture.wrapS = RepeatWrapping
  texture.wrapT = RepeatWrapping
  
  val cube =
    val geometry = BoxGeometry(0.3 * screenSettings.height, 0.3 * screenSettings.height, 0.3 * screenSettings.height)
    val material = MeshStandardMaterial(literal(map = texture))
    new Mesh(geometry, material)

  cube.position.z -= 0.15 * screenSettings.height
  
  val hemiLight = HemisphereLight(0xffffdd, 0x000000, 0.6)
  hemiLight.lookAt(screenSettings.height, 0, 0)

  val windowLight = DirectionalLight(0xddddff, 0.7)
  windowLight.position.z = screenSettings.height
  windowLight.position.x = -screenSettings.height

  add(cube)
  add(hemiLight)
  add(windowLight)
  
  def nextFrame(): Unit =
    cube.rotation.y += 0.1
  
  