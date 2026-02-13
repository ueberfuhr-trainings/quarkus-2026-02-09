package de.samples.quarkus.shared.interceptors;

import io.quarkus.arc.log.LoggerName;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;

@LogPerformance
@Interceptor
public class LogPerformanceInterceptor {

  @LoggerName("log-performance")
  Logger logger;

  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    long start = System.currentTimeMillis();
    try {
      return ctx.proceed();
    } finally {
      long end = System.currentTimeMillis();
      logger.infov(
        "Method {0} took {1}ms",
        ctx.getMethod().getName(),
        end - start
      );
    }
  }
}
