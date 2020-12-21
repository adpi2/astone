package astone

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import scalajs.three._

object Scene extends scalajs.three.Scene:
  val cube =
    val texture = new TextureLoader().load(tisTheSeason)
    texture.wrapS = RepeatWrapping
    texture.wrapT = RepeatWrapping
    val geometry = new BoxGeometry()
    val material = new MeshBasicMaterial(literal(map = texture))
    new Mesh(geometry, material)

  val line =
    val points = js.Array(
      new Vector3(-1.5, 0, 0),
      new Vector3(0, 1.5, 0),
      new Vector3(1.5, 0, 0)      
    )
    val geometry = new BufferGeometry()
    geometry.setFromPoints(points)
    val material = new LineBasicMaterial(literal(color = 0x0000ff))
    new Line(geometry, material)

  add(cube)
  add(line)
  
  def nextFrame(): Unit =
    line.rotation.y += 0.01
    cube.rotation.x += 0.002
    cube.rotation.y += 0.01
  
  