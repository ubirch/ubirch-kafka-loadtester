package com.ubirch.loadtest.producers


import com.ubirch.loadtest.Utils._

/**
  * Created by Bondarenko on 9/14/18.
  */
object SimpleProducer extends App with Producers with Generators {

  implicit val generator = RandomFiniteMessagesGenerator(1000, 10)

  implicit val producer = createProducer(loadProperties("producer.properties"))


  sendMessages("input")

}






