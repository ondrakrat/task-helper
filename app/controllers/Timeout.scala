package controllers

import scala.concurrent.{Await, Awaitable}
import scala.concurrent.duration.Duration

/**
  * @author Ondřej Kratochvíl
  */
trait Timeout {
  val timeoutInterval = Duration(1000, "millis")

  def async(awaitable: Awaitable[AnyRef]) = {
    Await.result(awaitable, timeoutInterval)
  }
}
