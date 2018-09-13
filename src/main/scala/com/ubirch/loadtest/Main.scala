package com.ubirch.loadtest

import org.slf4j.{Logger, LoggerFactory}

/**
  * Main startup code.
  *
  * @author Matthias L. Jugel
  */
object Main extends App {
  private val logger = LoggerFactory.getLogger("main")

  def startUp(): Unit = {
    logger.info("starting up ...")
    // do something
  }
}
