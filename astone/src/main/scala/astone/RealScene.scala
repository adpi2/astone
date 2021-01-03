package astone

import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model._

class RealScene(webcam: Webcam, screenWidth: Double, screenHeight: Double, headSize: Double) extends Scene:
  private val fov = Math.toDegrees(2 * Math.atan(webcam.height * 0.5 / webcam.focal))

  private val origin =
    val geometry = SphereGeometry(30, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0x0000ff))
    Mesh(geometry, material)

  private val webcamObj = PerspectiveCamera(fov, webcam.width / webcam.height, webcam.focal, 3 * webcam.focal)
  private val webcamHelper = CameraHelper(webcamObj)

  webcamObj.position.y = webcam.y
  webcamObj.lookAt(0, webcam.y, webcam.focal)

  private val screen =
    val geometry = PlaneGeometry(screenWidth, screenHeight)
    val material = MeshBasicMaterial(literal(color = 0x00ff00, side = DoubleSide))
    Mesh(geometry, material)

  private val headSphere =
    val geometry = SphereGeometry(0.5 * headSize, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0xff0000))
    Mesh(geometry, material)

  val headCam = PerspectiveCamera(fov, screenWidth / screenHeight, 0.1 * webcam.focal, 10 * webcam.focal)
  private val headCamHelper = CameraHelper(headCam)

  private val head =
    val group = Group()
    group.add(headSphere)
    group.add(headCam)
    group

  head.position.z = 2 * webcam.focal
  // counteract different front orientation of cameras vs group
  headCam.rotation.y = Math.PI
  head.lookAt(0, 0, 0)

  add(origin)
  add(webcamObj)
  add(webcamHelper)
  add(screen)
  add(head)
  add(headCamHelper)

  def computeHeadPosition(detection: FaceDetection): Unit =
    head.position.x = (0.5 * webcam.width - detection.x) * headSize / detection.scale
    head.position.y = (0.5 * webcam.height- detection.y) * headSize / detection.scale + webcam.y
    head.position.z = webcam.focal * headSize / detection.scale

    val fullWidth = webcam.width * head.position.z / webcam.focal
    val fullHeight = webcam.height * head.position.z / webcam.focal
    val x = 0.5 * (fullWidth - screenWidth) - head.position.x
    val y = 0.5 * (fullHeight - screenHeight) + head.position.y
    headCam.setViewOffset(fullWidth, fullHeight, x, y, screenWidth, screenHeight)
    headCamHelper.update()