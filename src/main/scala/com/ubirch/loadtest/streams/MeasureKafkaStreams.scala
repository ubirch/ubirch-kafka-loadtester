package com.ubirch.loadtest.streams

import com.lightbend.kafka.scala.streams.DefaultSerdes.stringSerde
import com.ubirch.loadtest.Measurements
import com.ubirch.loadtest.producers.{KafkaSource, MessageGenerator, RandomFiniteMessagesGenerator, SimpleStringProducer}
import com.ubirch.loadtest.producers.SimpleStringProducer._
import org.apache.kafka.streams.Consumed
import org.apache.kafka.streams.kstream.Produced

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Random

/**
  * Created by Bondarenko on 9/15/18.
  */
object MeasureKafkaStreams extends App with KafkaStreamsRunner with KafkaSteamsConfig with Measurements {
  override def clientId = s"client-id-${Random.nextInt(1000000)}"

  processMessages(1000000, "input", "output")


  def processMessages(messagesCount: Int, inputTopic: String, outputTopic: String) = {
    sendMessagesToSourceAsync(
      RandomFiniteMessagesGenerator(messagesCount, 100),
      "producer.properties")(inputTopic, messagesCount)
    val stat = mirrorMessagesToOutputTopic(messagesCount, inputTopic, outputTopic)

    logger.info(s"""== MESSAGES PROCESSING SUMMARY ==
                total messages: $messagesCount,
                total time spent: ${stat.totalTime},
                processing time from first message: ${stat.fromFirstMessageReadTime}
                average processing time: ${stat.avgMessageProcessingTime}
    """)
  }


  def sendMessagesToSourceAsync(messagesGenerator: MessageGenerator[String, String], producerPropertiesPath: String)(topicName: String, messagesCount: Int) = {
    Future {
      KafkaSource(topicName).produceMessagesToTopic(
        SimpleStringProducer.producer,
        messagesGenerator,
        producerFromProps(producerPropertiesPath)
      )
    }
  }

  def mirrorMessagesToOutputTopic(count: Int, inputTopic: String, outputTopic: String) = {
    val inputStream = builder.stream(inputTopic, Consumed.`with`(stringSerde, stringSerde))
    val output = inputStream.through(outputTopic, Produced.`with`(stringSerde, stringSerde))
    var messagesRead = 0
    var firstMessageReadTimestamp = 0L
    val beforeStart = System.currentTimeMillis()

    Future {
      output foreach { (k, v) =>
        if (firstMessageReadTimestamp == 0L) firstMessageReadTimestamp = System.currentTimeMillis()
        messagesRead = messagesRead + 1
        logger.debug(s"MESSAGE PROCESSED [$k]")
      }
      streams.start()
    }

    def totalTime = System.currentTimeMillis() - beforeStart
    def avgMessageProcessingTime = totalTime.toDouble / count.toDouble
    def fromFirstMessageReadTime = System.currentTimeMillis() - firstMessageReadTimestamp

    runWhile(messagesRead < count)(
      Stat(totalTime, fromFirstMessageReadTime, avgMessageProcessingTime)
    )

  }

  @tailrec
  private def runWhile[T](cond: => Boolean)(f: => T): T = {
    if (!cond) f else {
      Thread.sleep(100)
      runWhile(cond)(f)
    }
  }


  case class Stat(totalTime: Long, fromFirstMessageReadTime: Long, avgMessageProcessingTime: Double)

}


