package com.ubirch.loadtest

import org.slf4j.LoggerFactory

/**
  * Created by Bondarenko on 9/14/18.
  */
trait Logging {
  lazy val logger = LoggerFactory.getLogger(this.getClass)
}
