package astone

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model.FaceDetection

class RealScene(focal: Double, camWidth: Double, camHeight: Double, screenWidth: Double, screenHeight: Double, headSize: Double) extends Scene:
  private val fov = Math.toDegrees(2d * Math.atan(camHeight * 0.5 / focal))

  private val origin =
    val geometry = SphereGeometry(30d, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0x0000ff, wireframe = true))
    Mesh(geometry, material)

  private val webcam = PerspectiveCamera(fov, camWidth / camHeight, focal, 3d * focal)
  private val webcamHelper = CameraHelper(webcam)

  webcam.position.y = 0.5 * screenHeight
  webcam.lookAt(0d, 0.5 * screenHeight, focal)

  private val screen =
    val geometry = PlaneGeometry(screenWidth, screenHeight)
    val material = MeshBasicMaterial(literal(color = 0x00ff00, side = DoubleSide))
    Mesh(geometry, material)

  private val headSphere =
    val geometry = SphereGeometry(0.5 * headSize, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0xff0000))
    Mesh(geometry, material)

  val headCam = PerspectiveCamera(fov, screenWidth / screenHeight, focal, 3d * focal)
  private val headCamHelper = CameraHelper(headCam)

  private val head =
    val group = Group()
    group.add(headSphere)
    group.add(headCam)
    group

  head.position.z = 2d * focal
  // counteract different front orientation of cameras vs group
  headCam.rotation.y = Math.PI
  head.lookAt(0d, 0d, 0d)

  add(origin)
  add(webcam)
  add(webcamHelper)
  add(screen)
  add(head)
  add(headCamHelper)

  def computeHeadPosition(detection: FaceDetection): Unit =
    head.position.x = (0.5 * camWidth - detection.x) * headSize / detection.scale
    head.position.y = (0.5 * camHeight- detection.y) * headSize / detection.scale + 0.5 * screenHeight
    head.position.z = focal * headSize / detection.scale

    val fullWidth = camWidth * head.position.z / focal
    val fullHeight = camHeight * head.position.z / focal
    val x = 0.5 * (fullWidth - screenWidth) - head.position.x
    val y = 0.5 * (fullHeight - screenHeight) + head.position.y
    headCam.setViewOffset(fullWidth, fullHeight, x, y, screenWidth, screenHeight)
    headCamHelper.update()