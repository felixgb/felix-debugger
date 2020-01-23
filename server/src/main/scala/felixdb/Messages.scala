package felixdb

import upickle.default.{ReadWriter => RW, macroRW}

sealed abstract class DebugMessage

object DebugMessage {

  final case class Varible(
    name: String,
    `type`: String,
    value: String
  ) extends DebugMessage

  implicit val varibleRW: RW[Varible] = macroRW
  implicit val messageRW: RW[DebugMessage] = RW.merge(varibleRW)

}
