package com.ubirch.loadtest.streams

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.{Consumed, KafkaStreams, StreamsBuilder, StreamsConfig}
import com.lightbend.kafka.scala.streams.DefaultSerdes._
import com.ubirch.loadtest.{Executor, Logging}
import com.ubirch.loadtest.processors.TimeStampsProcessor

import scala.concurrent.duration._
import com.ubirch.loadtest.streams.KafkaStreamsRunner.{builder, streamsConfiguration}
import org.joda.time.DateTime

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.ArrayBuffer

/**
  * Created by Bondarenko on 9/14/18.
  */
object KafkaStreamsRunner extends App with KafkaSteamsConfig with Logging {


  override def clientId = s"client-id-1}"

  override val inputTopic = "input"

  val timeStamps = ArrayBuffer[Long]()


  val outputStream = inputStream.through("output", Produced.`with`(stringSerde, stringSerde))

  var minTime = 0L
  var maxTime = 0L





  var elapsed = 0L
  var n = 0L



//  Executor.exec(5000 second){
//    Range(0, 1000).foreach{ _ =>
//      Thread.sleep(1000)
//      if(n > 0) logger.info(s"AVG: ${elapsed / n}")
//    }
//
//  }

  outputStream.process(TimeStampsProcessor[String, String] { (processorContext, key, _) =>
    val timeStamp = processorContext.timestamp()
    n = if(n > 1000) 0 else (n + 1)
    if(n == 0) elapsed = System.currentTimeMillis() - timeStamp
    else elapsed = elapsed + System.currentTimeMillis() - timeStamp

    //logger.info(s"TIMESTAMP[$key]: ${System.currentTimeMillis() - timeStamp}")
  })

  streams.start()

  //Thread.sleep(5000)
  //logger.info(s"COMPLETED[$maxTime]: ${timeStamps.size}")

  //val processingTime = timeStamps.max - timeStamps.min

  //
  //logger.info(s"PROCESSING TIME: ${processingTime}")

  while (true) {
    Thread.sleep(1000)
    if(n > 0) logger.info(s"ELAPSED: ${elapsed}, $n, AVG: ${elapsed / n}")
  }

//  Range(0, 1000).foreach{ _ =>
//    Thread.sleep(1000)
//    if(n > 0) logger.info(s"ELAPSED: ${elapsed}, $n, AVG: ${elapsed / n}")
//  }

}

trait KafkaSteamsConfig {

  val inputTopic: String

  def clientId: String

  lazy val inputStream = builder.stream(inputTopic, Consumed.`with`(stringSerde, stringSerde))

  val streamsConfiguration: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, clientId)
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    p.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    p.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "10000")
    p.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    p.put(StreamsConfig.STATE_DIR_CONFIG, "temp")
    p
  }

  lazy val builder: StreamsBuilder = new StreamsBuilder()

  lazy val streams: KafkaStreams = new KafkaStreams(builder.build(), streamsConfiguration)

  //  Future{
  //    Range(0, 1000).foreach{ _ =>
  //      Thread.sleep(1000)
  //
  //      Thread.sleep(1000)
  //      streams.metrics().asScala.foreach{ case (metricName, metric) =>
  //        logger.info(s"METRIC [${metricName.description()}]: ${metric.metricValue()}")
  //      }
  //    }
  //  }


}
