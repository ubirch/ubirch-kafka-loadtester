package com.ubirch.loadtest

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by Bondarenko on 9/17/18.
  */
class UtilsTest extends WordSpec with Matchers  with ExternalCommands {
  "Bash command" should {
    "be executed successfully on *NIX systems" in {
      execBashCommand("ls -al") should equal(Right[String, Unit]())

    }
  }
}
