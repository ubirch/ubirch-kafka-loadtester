package com.ubirch.loadtest

import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import com.ubirch.loadtest.Utils._
import collection.JavaConverters._

/**
  * Created by Bondarenko on 9/14/18.
  */
object SimpleConsumer extends App with Logging {

  val consumer = new KafkaConsumer[String, String](loadProperties("consumer.properties"))

  consumer.subscribe(List("outputTopic1").asJava)





  consumer.poll(50000).iterator().asScala.toStream.foreach{ record => logger.info(record.key()) }
  



}
