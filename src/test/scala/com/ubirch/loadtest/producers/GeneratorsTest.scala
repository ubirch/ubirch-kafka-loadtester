package com.ubirch.loadtest.producers

import com.typesafe.scalalogging.StrictLogging
import org.scalatest._

/**
  * Created by Bondarenko on 9/14/18.
  */
class GeneratorsTest extends WordSpec with Matchers with StrictLogging with Generators{

  "Finite random generator" should {
    "generate fixed size strings" in {
      val gen = RandomFiniteMessagesGenerator(1, 100)
      val messages = gen.messages

      messages should have size(1)
      val (key, value) = messages.head
      key should equal("0")
      value should have size(100)

    }
  }

}
