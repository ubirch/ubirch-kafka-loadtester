package com.ubirch.loadtest

import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig

/**
  * Created by Bondarenko on 9/14/18.
  */
object KafkaStreamsRunner extends App{

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
}
