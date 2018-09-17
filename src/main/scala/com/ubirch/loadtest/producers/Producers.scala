package com.ubirch.loadtest.producers

import java.util.Properties

import com.ubirch.loadtest.Logging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import com.ubirch.loadtest.Utils._

/**
  * Created by Bondarenko on 9/14/18.
  */
trait Producers extends Logging {

  private def toProducerRecord(topic: String)(message: SimpleMessage): ProducerRecord[String, String] =
    new ProducerRecord[String, String](topic, message.key, message.value)

  def createProducer(propsPath: String) = new KafkaProducer[String, String](loadProperties(propsPath))


  def sendMessage(topic: String)(message: SimpleMessage)(implicit producer: KafkaProducer[String, String]) =
    producer.send(toProducerRecord(topic)(message))


  def sendMessages(topic: String)(implicit generator: MessageGenerator[String, String], producer: KafkaProducer[String, String]) = {
    generator.messages.foreach {
      case (k, v) =>
        sendMessage(topic)(SimpleMessage(k, v))
    }
    producer.flush()
    producer.close()
  }

}


object SimpleStringProducer extends Producers {
  implicit val producer = this

  def producerFromProps(propsPath: String) = createProducer(propsPath)
}