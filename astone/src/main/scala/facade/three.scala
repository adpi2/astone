package facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom.raw.HTMLCanvasElement

@JSImport("three", JSImport.Namespace)
@js.native
object three extends js.Object:
  @js.native
  trait Position extends js.Object:
    var x: Double = js.native
    var y: Double = js.native
    var z: Double = js.native

    def set(x: Double, y: Double, z: Double): Unit = js.native

  @js.native
  class Euler(var x: Double, var y: Double, var z: Double) extends js.Object:
    def set(x: Double, y: Double, z: Double): Unit = js.native

  @js.native
  class Vector2(var x: Double, var y: Double) extends js.Object:
    def set(x: Double, y: Double): Unit = js.native

  @js.native
  class Vector3(var x: Double, var y: Double, var z: Double) extends js.Object

  @js.native
  class Scene() extends js.Object:
    def add(object3d: Object3D): Unit = js.native

  @js.native
  class WebGLRenderer(parameters: js.Dynamic = null) extends js.Object:
    def setSize(width: Double, height: Double): Unit = js.native
    def domElement: HTMLCanvasElement = js.native
    def render(scene: Scene, camera: Camera): Unit = js.native

  @js.native
  trait Object3D extends js.Object:
    val position: Vector3 = js.native
    val rotation: Euler = js.native
    def lookAt(x: Double, y: Double, z: Double): Unit = js.native

  @js.native
  class Font(data: js.Object) extends js.Object

  /* Cameras */
  @js.native
  trait Camera extends Object3D

  @js.native
  class PerspectiveCamera(fov: Int, aspectRatio: Double, near: Double, far: Double) extends Camera

  @js.native
  class OrthographicCamera(left: Double, right: Double, top: Double, bottom: Double, near: Double, far: Double) extends Camera

  /* Geometries */
  @js.native
  trait Geometry extends js.Object

  @js.native
  class PlaneGeometry(
    width: Double = 1d, height: Double = 1d,
    widthSegments: Int = 1, heightSegments: Int = 1
  ) extends Geometry

  @js.native
  class BoxGeometry(width: Double, height: Double, depth: Double) extends Geometry

  @js.native
  class SphereGeometry(
    radius: Double = 1, widthSegments: Int = 8, heightSegments: Int = 6,
    phiStart: Double = 0, phiLength: Double = Math.PI * 2,
    thetaStart: Double = 0, thetaLength: Double =  Math.PI
  ) extends Geometry

  @js.native
  class TextGeometry(text: String, parameters: js.Dynamic) extends Geometry

  /* Buffer Geometries */
  @js.native
  class BufferGeometry() extends Geometry:
    def setFromPoints(points: js.Array[Vector3]): Unit = js.native

  @js.native
  trait Material extends js.Object

  @js.native
  class MeshBasicMaterial(parameters: js.Dynamic) extends Material

  @js.native
  class LineBasicMaterial(parameters: js.Dynamic) extends Material

  @js.native
  class LineDashedMaterial(parameters: js.Dynamic) extends Material

  @js.native
  class Mesh(geometry: Geometry, material: Material) extends Object3D

  @js.native
  class Line(geometry: Geometry, material: Material) extends Object3D:
    def computeLineDistances(): Line = js.native

  @js.native
  trait Wrapping extends js.Object
  
  @js.native
  object RepeatWrapping extends Wrapping

  @js.native
  trait Texture extends js.Object:
    var wrapS: Wrapping = js.native
    var wrapT: Wrapping = js.native
    val repeat: Vector2 = js.native

  @js.native
  class TextureLoader() extends js.Object:
    def load(url: String): Texture = js.native

  @js.native
  class FontLoader() extends js.Object:
    def load(url: String): Font = js.native

  /** Constants */
  val DoubleSide: js.Object = js.native
