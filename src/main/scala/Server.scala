package com.daniel

import Domain.{Data, State}

import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio.interop.catz._
import zio.stream.ZStream
import zio.stream.interop.fs2z._
import zio.{Ref, Task}

import scala.language.higherKinds

object Server {

  def stream: ZStream[State, Throwable, Nothing] = for {
    state <- ZStream.service[State]
    server <- BlazeServerBuilder[Task]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(wordCountRoute(state.windowRef).orNotFound)
      .serve
      .drain
      .toZStream()
  } yield server

  private def wordCountRoute(state: Ref[Seq[Data]]): HttpRoutes[Task] = {
    val dsl = new Http4sDsl[Task] {}
    import dsl._
    HttpRoutes.strict[Task] { case GET -> Root =>
      state.get.map(Service.calculateFrequency).flatMap(Ok(_))
    }
  }
}
