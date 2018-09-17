package com.ubirch.loadtest

/**
  * Created by Bondarenko on 9/16/18.
  */
trait Measurements {

  def measure[T](task: =>T):(T, Long) = {
    val startTime = System.currentTimeMillis()
    val taskResult = task
    val duration = System.currentTimeMillis() - startTime
    taskResult -> duration
  }
}
