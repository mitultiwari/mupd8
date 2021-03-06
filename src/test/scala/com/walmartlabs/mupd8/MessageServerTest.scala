/**
 * Copyright 2011-2012 @WalmartLabs, a division of Wal-Mart Stores, Inc.
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
 * 
 */

package com.walmartlabs.mupd8

import org.junit.runner.RunWith
import org.junit.Assert._
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import java.util.ArrayList
import scala.util.Random

@RunWith(classOf[JUnitRunner])
class MessageServerTest extends FunSuite {
  
  test("MessageServer/Client update remove") {
    
    val random = new Random(0)
	val array = new ArrayList[String]
    def storeMsg(msg : String ) = {
      array.add(msg)
    }
    
    val server = new MessageServerThread(4568)
    val sThread = new Thread(Misc.run(server.run))
    sThread.start
    val client = new MessageServerClient(storeMsg, "localhost", 4568, 50L)
    val t = new Thread(client)
    t.start
    (0 until 5).foreach { i =>
      client.addMessage("machine" + random.nextInt(10) + ".example.com")
    }
    for (i <- 0 until array.size()) {
      val tokens = array.get(i).trim.split("[ \n\t]")
      assertTrue(tokens(0).toInt == (i + 1))
    }
    
  }

}
