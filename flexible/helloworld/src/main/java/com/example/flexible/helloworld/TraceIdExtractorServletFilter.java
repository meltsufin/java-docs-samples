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

import com.google.cloud.logging.TraceLoggingEnhancer;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "traceIdExtractor", urlPatterns = {"/*"})
public class TraceIdExtractorServletFilter implements Filter {

  public static final String X_CLOUD_TRACE_HEADER = "x-cloud-trace-context";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain chain) throws IOException, ServletException {
    String traceId = extractTraceIdFromRequest((HttpServletRequest) servletRequest);

    if (traceId == null) {
      traceId = UUID.randomUUID().toString();
    }

    TraceLoggingEnhancer.setCurrentTraceId(traceId);

    chain.doFilter(servletRequest, servletResponse);
  }

  public String extractTraceIdFromRequest(HttpServletRequest req) {
    String traceId = req.getHeader(X_CLOUD_TRACE_HEADER);

    if (traceId != null) {
      int slash = traceId.indexOf('/');
      if (slash >= 0) {
        traceId = traceId.substring(0, slash);
      }
    }
    return traceId;
  }


  public void destroy() {
    TraceLoggingEnhancer.setCurrentTraceId(null);
  }

}
