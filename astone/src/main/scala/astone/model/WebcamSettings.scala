package astone.model

case class WebcamSettings(y: Double, diagViewAngle: Double, width: Int, height: Int):
  val diag = Math.sqrt(width * width + height * height)
  val focal = 0.5 * diag / Math.tan(Math.toRadians(diagViewAngle) / 2)
