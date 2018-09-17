package com.ubirch.loadtest.streams

import com.lightbend.kafka.scala.streams.DefaultSerdes.stringSerde
import com.ubirch.loadtest.Measurements
import com.ubirch.loadtest.producers.{KafkaSource, RandomFiniteMessagesGenerator, SimpleStringProducer}
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
  override def clientId = s"client-id-111"

  val inputTopicName = "input"
  val messagesCount = 100000

  //  Future {
  //    KafkaSource(inputTopicName).produceMessagesToTopic(
  //      SimpleStringProducer.p,
  //      RandomFiniteMessagesGenerator(messagesCount, 10),
  //      producerFromProps("producer.properties")
  //    )
  //  }


  Future {
    sendMessagesToSource
  }

  val (stat, duration) =
  measure{
    mirrorMessages(messagesCount)
  }
  logger.info(s"MESSAGES READ: $messagesCount, total time spent: $duration, processing time from first message: ${stat.fromFirstMessageReadTime}, total: ${stat.totalTime}")




  //  waitFor(100 second,
  //    collectStat(inputStream(inputTopicName), messagesCount)(outputStream("output"))
  //  ) { stat =>
  //     if(!stat.isEmpty) {
  //
  //       val creationMessagesDuration = stat.map(_.creationTimeStamp).max - stat.map(_.creationTimeStamp).min
  //       val avgProcessingTime = stat.map(_.processingTime).sum / stat.size
  //
  //       logger.info(s"MESSAGES SENDING TIME: ${creationMessagesDuration}")
  //       logger.info(s"AVERAGE PROCESSING TIME: ${avgProcessingTime}")
  //     }
  //  }


  def sendMessagesToSource = {
    KafkaSource(inputTopicName).produceMessagesToTopic(
      SimpleStringProducer.producer,
      RandomFiniteMessagesGenerator(messagesCount, 10),
      producerFromProps("producer.properties")
    )
  }

  def mirrorMessages(count: Int) = {
    val inputStream = builder.stream(inputTopicName, Consumed.`with`(stringSerde, stringSerde))
    val output = inputStream.through("output", Produced.`with`(stringSerde, stringSerde))
    var messagesRead = 0
    var firstMessageReadTimestamp = 0L
    val beforeStart = System.currentTimeMillis()

    Future {
      output foreach { (k, v) =>
        if(firstMessageReadTimestamp == 0L) firstMessageReadTimestamp = System.currentTimeMillis()
        messagesRead = messagesRead + 1
        logger.info(k)
      }
      streams.start()
    }

    runWhile(messagesRead < count)(Stat(System.currentTimeMillis() - beforeStart, System.currentTimeMillis() - firstMessageReadTimestamp))

  }

  @tailrec
  private def runWhile[T](cond: => Boolean)(f: => T): T = {
    if (!cond) f else {
      Thread.sleep(1000)
      runWhile(cond)(f)
    }
  }


  case class Stat(totalTime: Long, fromFirstMessageReadTime: Long)

}


