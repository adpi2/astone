package astone

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model.FaceDetection

class RealScene(focal: Double, width: Double, height: Double, headSize: Double) extends Scene:
  private val fov = Math.toDegrees(2d * Math.atan(height * 0.5 / focal))

  private val origin =
    val geometry = SphereGeometry(30d, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0x0000ff))
    Mesh(geometry, material)

  private val webcam = PerspectiveCamera(fov, width / height, focal, 3d * focal)
  private val webcamHelper = CameraHelper(webcam)

  webcam.position.y = 0.5 * height
  webcam.lookAt(0d, 0.5 * height, focal)

  private val screen =
    val geometry = PlaneGeometry(width, height)
    val material = MeshBasicMaterial(literal(color = 0x00ff00, side = DoubleSide))
    Mesh(geometry, material)

  private val head =
    val geometry = SphereGeometry(0.5 * headSize, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0xff0000))
    Mesh(geometry, material)

  head.position.z = focal

  add(origin)
  add(webcam)
  add(webcamHelper)
  add(screen)
  add(head)

  def computeHeadPosition(detection: FaceDetection): Unit =
    head.position.x = (0.5 * width - detection.x) * headSize / detection.scale
    head.position.y = (0.5 * height- detection.y) * headSize / detection.scale + 0.5 * height
    head.position.z = focal * headSize / detection.scale
