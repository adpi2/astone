package facade

import scala.language.implicitConversions
import scala.scalajs.js

import org.scalajs.dom.raw.HTMLMediaElement
import org.scalajs.dom.experimental.mediastream.MediaStream

object web:
  implicit def withSrcObject(video: HTMLMediaElement): WithSrcObject = video.asInstanceOf[WithSrcObject]

  @js.native
  trait WithSrcObject extends js.Object:
    var srcObject: MediaStream = js.native
