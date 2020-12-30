package astone

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Dynamic.literal
import facade.three._
import astone.model.FaceDetection

class RealScene(width: Double, height: Double, headSize: Double) extends Scene:
  private val diagViewAngle = Math.toRadians(78D)
  private val diag = Math.sqrt(width * width + height * height)
  private val focal = diag / (2D * Math.tan(diagViewAngle / 2D))
  
  private val focalAngle =
    val points = js.Array(
      new Vector3(-width, 0D, 2D * focal),
      new Vector3(0D, 0D, 0D),
      new Vector3(width, 0D, 2D * focal)
    )
    val geometry = BufferGeometry()
    geometry.setFromPoints(points)
    val material = LineDashedMaterial(literal(color = 0x00ffff, dashSize = 30, gapSize = 30))
    Line(geometry, material).computeLineDistances()
    

  private val screen =
    val geometry = PlaneGeometry(width, height)
    val material = MeshBasicMaterial(literal(color = 0x00ff00, side = DoubleSide))
    println(DoubleSide)
    Mesh(geometry, material)
  
  screen.position.z = focal
  screen.position.y = - height / 2d

  private val head =
    val geometry = SphereGeometry(headSize / 2D, 10, 10)
    val material = MeshBasicMaterial(literal(color = 0xff0000))
    Mesh(geometry, material)

  head.position.z = 300

  add(focalAngle)
  add(screen)
  add(head)

  def computeHeadPosition(detection: FaceDetection): Unit =
    head.position.x = (width / 2D - detection.x) * headSize / detection.scale
    head.position.y = (height / 2D - detection.y) * headSize / detection.scale
    head.position.z = focal * headSize / detection.scale
    
  
  