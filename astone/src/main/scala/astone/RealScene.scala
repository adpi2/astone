package astone

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model.FaceDetection

class RealScene(focal: Double, width: Double, height: Double, headSize: Double) extends Scene:
  private val fov = Math.toDegrees(2d * Math.atan(height / (2d * focal)))

  private val origin =
    val geometry = SphereGeometry(30d, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0x0000ff))
    Mesh(geometry, material)

  private val webcam = PerspectiveCamera(fov, width / height, focal, 3d * focal)
  private val webcamHelper = CameraHelper(webcam)

  webcam.lookAt(0d, 0d, focal)

  private val screen =
    val geometry = PlaneGeometry(width, height)
    val material = MeshBasicMaterial(literal(color = 0x00ff00, side = DoubleSide))
    Mesh(geometry, material)
  
  screen.position.y = - height / 2d

  private val head =
    val geometry = SphereGeometry(headSize / 2D, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0xff0000))
    Mesh(geometry, material)

  head.position.z = 300

  add(origin)
  add(webcam)
  add(webcamHelper)
  add(screen)
  add(head)

  def computeHeadPosition(detection: FaceDetection): Unit =
    head.position.x = (width / 2D - detection.x) * headSize / detection.scale
    head.position.y = (height / 2D - detection.y) * headSize / detection.scale
    head.position.z = focal * headSize / detection.scale
