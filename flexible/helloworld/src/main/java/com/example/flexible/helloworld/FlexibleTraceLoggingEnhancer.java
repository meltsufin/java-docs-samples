/*
 *  Copyright 2018 original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.flexible.helloworld;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.TraceLoggingEnhancer;

public class FlexibleTraceLoggingEnhancer extends TraceLoggingEnhancer {

  private static final String APP_ENGINE_LABEL_NAME = "appengine.googleapis.com/trace_id";

  private boolean runningOnAppEngine = System.getenv("GAE_INSTANCE") != null;

  @Override
  public void enhanceLogEntry(LogEntry.Builder builder) {
    super.enhanceLogEntry(builder);

    if (getCurrentTraceId() != null && this.runningOnAppEngine) {
      builder.addLabel(APP_ENGINE_LABEL_NAME, getCurrentTraceId());
    }
  }
}
