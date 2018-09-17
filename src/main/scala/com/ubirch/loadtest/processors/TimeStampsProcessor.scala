package com.ubirch.loadtest.processors

import org.apache.kafka.streams.processor.{AbstractProcessor, Processor, ProcessorContext, ProcessorSupplier}

/**
  * Created by Bondarenko on 9/14/18.
  */
case class TimeStampsProcessor[K, V](action: (ProcessorContext, K, V) => Unit) extends ProcessorSupplier[K, V]{
  override def get(): Processor[K, V] = TimeProcessor(action)
}

case class TimeProcessor[K, V](action: (ProcessorContext, K, V) => Unit) extends AbstractProcessor[K, V]{
  override def process(key: K, value: V): Unit = {
    action(context(), key, value)
    context().forward(key , value)
  }

  override def punctuate(timestamp: Long): Unit = {

  }
}



