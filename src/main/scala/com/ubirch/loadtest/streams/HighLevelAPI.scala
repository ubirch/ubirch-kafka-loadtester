package com.ubirch.loadtest.streams

import com.ubirch.loadtest.{KafkaCommands, Measurements}
import com.ubirch.loadtest.producers.RandomFiniteMessagesGenerator
import com.ubirch.loadtest.streams.MeasureKafkaStreams.{logger, mirrorMessagesToOutputTopic, sendMessagesToSourceAsync}

import scala.util.Random

/**
  * Created by Bondarenko on 9/17/18.
  */
trait HighLevelAPI extends KafkaStreamsRunner
  with KafkaSteamsConfig
  with Measurements
  with KafkaCommands{

  /*
    Prerequisite:
      - kafka services should be running locally on default ports
      - listeners = PLAINTEXT://0.0.0.0:9092 in server.properties

   */

  def inputTopic = "input"

  def outputTopic = "output"

  override def clientId: String = s"client-id-${Random.nextInt(1000000)}"

  def prepare() = {
    createTopics(List(inputTopic, outputTopic))
  }

  def cleanUp() = {
    removeTopics(List(inputTopic, outputTopic))
  }

  def processMessages(messagesCount: Int) = {
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


}
