/*
 * Copyright 2018 ubirch GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ubirch.loadtest

import org.slf4j.LoggerFactory

/**
  * Main startup code.
  *
  * @author Matthias L. Jugel
  */
object Main extends App {
  private val logger = LoggerFactory.getLogger("main")

  def startUp(): Unit = {
    logger.info("starting up ...")
    // do something
  }

  startUp()
}
