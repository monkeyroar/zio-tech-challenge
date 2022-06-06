package com.daniel

import Blackbox.blackboxStream
import Domain.{Data, State}

import io.circe.parser._
import zio.Console._
import zio.{Ref, Task, ZIO, ZIOAppDefault, ZLayer}

object Main extends ZIOAppDefault {

  private val dataInputStream = blackboxStream
    .map(decode[Data])
    .collectRight
    .map(_.copy(timestamp = System.currentTimeMillis() / 1000))

  private val dataProcessingIO: ZIO[Ref[State], Throwable, Unit] = for {
    ref <- ZIO.service[Ref[State]]
    _   <- dataInputStream.foreach(updateWindow(_, ref))
  } yield ()
  private val serverIO: ZIO[Ref[State], Throwable, Unit] = Server.stream.runDrain
  private val mainIO                                     = dataProcessingIO.zipPar(serverIO)
  private val dependencies                               = ZLayer(Ref.make(State()))

  def run: Task[Unit] = mainIO.provide(dependencies)

  private def updateWindow(newData: Data, ref: Ref[State]): Task[Unit] = for {
    _        <- ref.update(Service.updateState(newData, _))
    newState <- ref.get
    _        <- printLine(s"$newData, state: $newState")
  } yield ()
}
