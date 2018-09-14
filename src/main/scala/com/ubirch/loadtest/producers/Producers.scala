package com.ubirch.loadtest.producers

import java.util.Properties

import com.ubirch.loadtest.Logging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Created by Bondarenko on 9/14/18.
  */
trait Producers extends Logging{

  self: Generators =>

  private def toProducerRecord(topic: String)(message: SimpleMessage): ProducerRecord[String, String] =
    new ProducerRecord[String, String](topic, message.key, message.value)

  def createProducer(props: Properties) = new KafkaProducer[String, String](props)

  def sendMessage(topic: String)(message: SimpleMessage)(implicit producer: KafkaProducer[String, String]) =
    producer.send(toProducerRecord(topic)(message))


  def sendMessages(topic: String)(implicit generator: MessageGenerator[String, String], producer: KafkaProducer[String, String]) =
    generator.messages.foreach {
      case (k, v) =>
        logger.info(s"sending message [${k}]")
        sendMessage(topic)(SimpleMessage(k, v))
    }
}
