package io.chrisdavenport.log4catswriterexample

import cats.implicits._
import cats.effect._
import cats.data._
import cats._
import io.chrisdavenport.log4cats._
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import io.chrisdavenport.log4cats.extras._

object Main extends IOApp {
  val chainLogger : Logger[Writer[Chain[LogMessage], ?]] = WriterLogger[Chain]()

  def run(args: List[String]): IO[ExitCode] = for {
    implicit0(l: Logger[IO]) <- Slf4jLogger.create[IO]
    _ <- WriterLogger.run[IO, Chain].apply(to10(0))
  } yield ExitCode.Success


  def to10(current: Int = 0): Writer[Chain[LogMessage], Int] =
    if (current >= 10) Writer.value[Chain[LogMessage], Int](current)
    else add1(current).flatMap(to10)

  def add1(i: Int): Writer[Chain[LogMessage], Int] = 
    chainLogger.info(s"Adding 1 to $i")
      .as(i + 1)
      .flatTap(i2 => chainLogger.info(s"After Adding 1 to value, value was $i2"))

}