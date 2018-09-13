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

import com.typesafe.scalalogging.StrictLogging
import org.json4s.DefaultFormats
import org.scalatest._

/**
  * Test the basic functional blocks of the load test.
  *
  * @author Matthias L. Jugel
  */
class LoadTestTest extends WordSpec with Matchers with StrictLogging {
  implicit val formats: DefaultFormats.type = DefaultFormats

  "A LoadTest" should {
    "do something" in {
      1 + 1 shouldBe 2
    }
  }
}
