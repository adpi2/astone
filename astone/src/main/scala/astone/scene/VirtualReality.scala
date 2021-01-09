package astone.scene

import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model._
  
class VirtualReality(webcamSettings: WebcamSettings, screenSettings: ScreenSettings, headSize: Double) extends Scene:
  val fov = Math.toDegrees(2 * Math.atan(webcamSettings.height * 0.5 / webcamSettings.focal))
    
  val origin =
    val geometry = SphereGeometry(30, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0x0000ff))
    Mesh(geometry, material)

  val webcam = PerspectiveCamera(fov, webcamSettings.width / webcamSettings.height, webcamSettings.focal, 10 * webcamSettings.focal)
  val webcamHelper = CameraHelper(webcam)
  webcam.position.y = webcamSettings.y
  webcam.lookAt(0, webcamSettings.y, webcamSettings.focal)

  val screen =
    val geometry = PlaneGeometry(screenSettings.width, screenSettings.height)
    val material = MeshBasicMaterial(literal(color = 0x00ff00, side = DoubleSide))
    Mesh(geometry, material)

  val headSphere =
    val geometry = SphereGeometry(0.5 * headSize, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0xff0000))
    Mesh(geometry, material)

  val headCam = PerspectiveCamera(fov, screenSettings.width / screenSettings.height, 0.1 * webcamSettings.focal, 10 * webcamSettings.focal)
  val headCamHelper = CameraHelper(headCam)

  val head =
    val group = Group()
    group.add(headSphere)
    group.add(headCam)
    group

  head.position.z = 2 * webcamSettings.focal
  // counteract different front orientation of cameras vs group
  headCam.rotation.y = Math.PI
  head.lookAt(0, 0, 0)
  
  add(origin)
  add(webcam)
  add(webcamHelper)
  add(screen)
  add(head)
  add(headCamHelper)

  def onHeadMoved(x: Double, y: Double, z: Double): Unit =
    head.position.x = x
    head.position.y = y
    head.position.z = z

    val fullWidth = webcamSettings.width * z / webcamSettings.focal
    val fullHeight = webcamSettings.height * z / webcamSettings.focal
    val offsetX = 0.5 * (fullWidth - screenSettings.width) - x
    val offsetY = 0.5 * (fullHeight - screenSettings.height) + y
    headCam.setViewOffset(fullWidth, fullHeight, offsetX, offsetY, screenSettings.width, screenSettings.height)
    headCamHelper.update()