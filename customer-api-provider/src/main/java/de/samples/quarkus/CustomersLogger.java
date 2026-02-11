package de.samples.quarkus;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.runtime.Startup;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

// @Dependent
@RequiredArgsConstructor
public class CustomersLogger {

  private final CustomersService customersService;

  @LoggerName("customers")
  Logger log;

  @Startup
  public void logCountOfCustomers() {
    var count = customersService.count();
    log.infov("Customers count: {0}", count);
  }

}
