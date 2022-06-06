package com.daniel

import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

object Domain {

  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  @ConfiguredJsonCodec case class Data(eventType: String, data: String, timestamp: Long)

  case class State(window: Seq[Data] = Seq())
}
