package com.ubirch.loadtest.producers

import scala.util.Random

/**
  * Created by Bondarenko on 9/14/18.
  */
trait Generators {
  sealed trait MessageGenerator[K, V] {
    def messages: Stream[(K, V)]
  }

  case class RandomFiniteMessagesGenerator(messagesCount: Int, messageSize: Int) extends MessageGenerator[String, String] {
    override def messages: Stream[(String, String)] = Range(0, messagesCount)
      .map { index =>
        index.toString -> Random.nextString(messageSize)
      }.toStream
  }
}
