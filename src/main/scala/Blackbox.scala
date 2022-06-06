package com.daniel

import zio.stream.{Stream, ZPipeline, ZStream}
import zio.{Schedule, ZIO, durationInt}

import java.io.{FileInputStream, IOException}

object Blackbox {
  val blackboxStream: Stream[IOException, String] = open("blackbox.out")

  private def open(filename: String): Stream[IOException, String] =
    ZStream
      .fromInputStreamZIO[Any](ZIO.attempt(new FileInputStream(filename)).refineToOrDie[IOException])
      .via(ZPipeline.utf8Decode)
      .via(ZPipeline.splitLines)
      .schedule(Schedule.spaced(1.second))
}
