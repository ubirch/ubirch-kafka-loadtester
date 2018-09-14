package com.ubirch.loadtest

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by Bondarenko on 9/14/18.
  */
object Executor {

  def exec[T](timeout: Duration)(func: =>T) = {

    val future = Future{
      val result = func
      result
    }

    Await.result(future, timeout)

  }



}
