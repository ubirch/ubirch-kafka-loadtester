package com.ubirch.loadtest

import java.io.FileInputStream
import java.util.Properties

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
