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

  private val dataProcessingIO: ZIO[State, Throwable, Unit] = for {
    state <- ZIO.service[State]
    _     <- dataInputStream.foreach(updateWindow(_, state.windowRef))
  } yield ()
  private val serverIO: ZIO[State, Throwable, Unit] = Server.stream.runDrain
  private val mainIO                                = dataProcessingIO.zipPar(serverIO)
  private val dependencies                          = ZLayer(Ref.make(Seq.empty[Data]).map(State))

  def run: Task[Unit] = mainIO.provide(dependencies)

  private def updateWindow(newData: Data, ref: Ref[Seq[Data]]): Task[Unit] = for {
    newState <- ref.modify(state => tupled(Service.updateState(newData, state)))
    _        <- printLine(s"$newData, state: $newState")
  } yield ()

  private def tupled[A](a: A): (A, A) = (a, a)
}
