package com.ubirch.loadtest

import java.io.FileInputStream
import java.util.Properties
import sys.process._

/**
  * Created by Bondarenko on 9/14/18.
  */
object Utils {
  def loadProperties(fileName: String) = {
    val props = new Properties()
    props.load(new FileInputStream(fileName))
    props
  }
}


trait ExternalCommands {

  def execBashCommand(cmd: String): Either[String, Unit] = {
    val result = cmd !

    Either.cond[String, Unit](result == 0,
      (),
      s"Error executing external command '$cmd'. Error code: $result"
    )


  }

  def execCommands[T](input: List[T])(cmdFunc: T => String): Either[List[String], Unit] = {
    input
      .map { arg =>
        cmdFunc(arg)
      }.map(cmd => execBashCommand(cmd))
      .collect { case Left(error) => error } match {
      case Nil => Right[List[String], Unit]()
      case errors => Left[List[String], Unit](errors)
    }

  }
}

trait KafkaCommands extends ExternalCommands {

  def createTopics(topics: List[String]) = execCommands(topics){ topic =>
    s"kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic $topic"
  }

  def removeTopics(topics: List[String]) = execCommands(topics){ topic =>
    s"kafka-topics --zookeeper localhost:2181 --delete --topic $topic"
  }

}

