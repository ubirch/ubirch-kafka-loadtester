package com.ubirch.loadtest.producers


import com.ubirch.loadtest.Utils._
import org.apache.kafka.clients.producer.KafkaProducer

/**
  * Created by Bondarenko on 9/14/18.
  */
object SimpleProducer extends App with Producers with Generators {

  implicit val generator = RandomFiniteMessagesGenerator(1000, 10)

  implicit val producer = createProducer("producer.properties")


  sendMessages("input")

}

case class KafkaSource(topic: String) {

  def produceMessagesToTopic(implicit
                             producers: Producers,
                             generator: MessageGenerator[String, String],
                             producer: KafkaProducer[String, String]) = producers.sendMessages(topic)(generator, producer)

}




