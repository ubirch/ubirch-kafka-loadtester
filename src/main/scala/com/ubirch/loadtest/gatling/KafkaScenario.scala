package com.ubirch.loadtest.gatling

import io.gatling.core.ConfigKeys.http

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._

/**
  * Created by Bondarenko on 9/14/18.
  */
object KafkaScenario extends Simulation{
  scenario("simple load test")
}
