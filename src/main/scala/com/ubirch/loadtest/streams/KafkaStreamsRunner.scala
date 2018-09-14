package com.ubirch.loadtest.streams

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.{Consumed, KafkaStreams, StreamsBuilder, StreamsConfig}
import com.lightbend.kafka.scala.streams.DefaultSerdes._
import com.ubirch.loadtest.Logging

import scala.collection.JavaConverters._

/**
  * Created by Bondarenko on 9/14/18.
  */
object KafkaStreamsRunner extends App with KafkaSteamsConfig with Logging{

  val inputStream = builder.stream("inputTopic1", Consumed.`with`(stringSerde, stringSerde))
  val streams: KafkaStreams = new KafkaStreams(builder.build(), streamsConfiguration)

  val outputStream = inputStream.through("outputTopic1", Produced.`with`(stringSerde, stringSerde))

  outputStream.foreach(  (k, v) => logger.info(s"${v}") )

  streams.metrics().asScala.foreach{ case (metricName, metric) =>
      logger.info(s"METRIC [${metricName.description()}]: ${metric.metricValue()}")
  }


  streams.start()

}

trait KafkaSteamsConfig {
  val streamsConfiguration: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "id-config-1")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    p.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    p.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "10000")
    p.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    p.put(StreamsConfig.STATE_DIR_CONFIG, "temp")
    p
  }

  val builder: StreamsBuilder = new StreamsBuilder()


}
