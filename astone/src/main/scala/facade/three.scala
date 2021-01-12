package facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import org.scalajs.dom.html.Canvas

@JSImport("three", JSImport.Namespace)
@js.native
object three extends js.Object:
  /* Cameras */
  @js.native
  class Camera() extends Object3D

  @js.native
  class OrthographicCamera(left: Double, right: Double, top: Double, bottom: Double, near: Double, far: Double) extends Camera()

  @js.native
  class PerspectiveCamera(fov: Double, aspectRatio: Double, near: Double, far: Double) extends Camera():
    def setViewOffset(fullWidth: Double, fullHeight: Double, x: Double, y: Double, width: Double, height: Double): Unit =
      js.native

    def updateProjectionMatrix(): Unit = js.native

  
  /* Constants */
  val DoubleSide: js.Object = js.native

  trait Wrapping extends js.Object

  val RepeatWrapping: Wrapping = js.native


  /* Core */
  @js.native
  class BufferGeometry() extends Geometry:
    def setFromPoints(points: js.Array[Vector3]): Unit = js.native

  @js.native
  class Geometry() extends js.Object

  @js.native
  class Object3D() extends js.Object:
    val position: Vector3 = js.native
    val rotation: Euler = js.native
    def lookAt(x: Double, y: Double, z: Double): Unit = js.native
    def updateMatrix(): Unit = js.native
    def updateMatrixWorld(force: Boolean): Unit = js.native
    def updateWorldMatrix(updateParents: Boolean, updateChildren: Boolean): Unit = js.native


  /* Extras / Core */
  @js.native
  class Font(data: js.Object) extends js.Object


  /* Geometries */
  @js.native
  class BoxGeometry(width: Double, height: Double, depth: Double) extends Geometry()

  @js.native
  class PlaneGeometry(
    width: Double = 1d, height: Double = 1d,
    widthSegments: Int = 1, heightSegments: Int = 1
  ) extends Geometry():
    def parameters: js.Object = js.native

  @js.native
  class SphereGeometry(
    radius: Double = 1, widthSegments: Int = 8, heightSegments: Int = 6,
    phiStart: Double = 0, phiLength: Double = Math.PI * 2,
    thetaStart: Double = 0, thetaLength: Double =  Math.PI
  ) extends Geometry()

  @js.native
  class TextGeometry(text: String, parameters: js.Dynamic) extends Geometry()


  /* Helpers */
  @js.native
  class CameraHelper(camera: Camera) extends LineSegments(js.native, js.native):
    def pointMap: js.Object = js.native
    def update(): Unit = js.native
    var matrixAutoUpdate: Boolean = js.native

  
  /* Lights */
  @js.native
  class DirectionalLight(color: Int, intensity: Double) extends Light(js.native, js.native)

  @js.native
  class Light(color: Int, intensity: Double) extends Object3D

  @js.native
  class HemisphereLight(skyColor: Int = 0xffffff, groundColor: Int = 0xffffff, intensity: Double = 1d)
    extends Light(js.native, js.native)
  
  
  /* Loaders */
  @js.native
  class FontLoader() extends js.Object:
    def load(url: String): Font = js.native
  
  @js.native
  class TextureLoader() extends js.Object:
    def load(url: String): Texture = js.native


  /* Materials */
  @js.native
  class LineBasicMaterial(parameters: js.Dynamic) extends Material()

  @js.native
  class LineDashedMaterial(parameters: js.Dynamic) extends Material()
  
  @js.native
  class Material() extends js.Object

  @js.native
  class MeshBasicMaterial(parameters: js.Dynamic) extends Material()

  @js.native
  class MeshStandardMaterial(parameters: js.Dynamic) extends Material()


  /* Maths */
  @js.native
  class Euler(var x: Double, var y: Double, var z: Double) extends js.Object:
    def set(x: Double, y: Double, z: Double): Unit = js.native

  @js.native
  class Vector2(var x: Double, var y: Double) extends js.Object:
    def set(x: Double, y: Double): Unit = js.native

  @js.native
  class Vector3(var x: Double, var y: Double, var z: Double) extends js.Object


  /* Objects */
  @js.native
  class Group() extends Object3D:
    def add(obj: Object3D): Unit = js.native

  @js.native
  class Line(geometry: Geometry, material: Material) extends Object3D:
    def computeLineDistances(): Line = js.native

  @js.native
  class LineSegments(geometry: Geometry, material: Material) extends Object3D
  
  @js.native
  class Mesh(geometry: Geometry, material: Material) extends Object3D


  /* Renderers */
  @js.native
  class WebGLRenderer(parameters: js.Dynamic = null) extends js.Object:
    def setSize(width: Double, height: Double): Unit = js.native
    def domElement: Canvas = js.native
    def render(scene: Scene, camera: Camera): Unit = js.native
    // var context: 


  /* Scenes */
  @js.native
  class Scene() extends js.Object:
    def add(object3d: Object3D): Unit = js.native

  
  /* Textures */
  @js.native
  trait Texture extends js.Object:
    var wrapS: Wrapping = js.native
    var wrapT: Wrapping = js.native
    val repeat: Vector2 = js.native
