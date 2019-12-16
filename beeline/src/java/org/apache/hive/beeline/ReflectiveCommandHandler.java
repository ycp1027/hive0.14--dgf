/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This source file is based on code taken from SQLLine 1.0.2
 * See SQLLine notice in LICENSE
 */
package org.apache.hive.beeline;

import jline.Completor;

import org.apache.hadoop.fs.shell.Command;

/**
 * A {@link Command} implementation that uses reflection to
 * determine the method to dispatch the command.
 *
 */
public class ReflectiveCommandHandler extends AbstractCommandHandler {
  private final BeeLine beeLine;

  public ReflectiveCommandHandler(BeeLine beeLine, String[] cmds, Completor[] completor) {
    super(beeLine, cmds, beeLine.loc("help-" + cmds[0]), completor);
    this.beeLine = beeLine;
  }

  public boolean execute(String line) {
    lastException = null;
    try {
      Object ob = beeLine.getCommands().getClass().getMethod(getName(),
          new Class[] {String.class})
          .invoke(beeLine.getCommands(), new Object[] {line});
      return ob != null && ob instanceof Boolean
          && ((Boolean) ob).booleanValue();
    } catch (Throwable e) {
      lastException = e;
      return beeLine.error(e);
    }
  }
}