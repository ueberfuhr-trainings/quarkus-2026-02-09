package de.samples.quarkus;

import io.quarkus.arc.log.LoggerName;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

// Anforderung:
// - Property application.customers-logger.enabled=true|false (default: false)
// - Property application.customers-logger.startup.message=<string>

// @Dependent
@IfBuildProperty(
  name = "application.customers-logger.enabled",
  stringValue = "true"
)
@RequiredArgsConstructor
public class CustomersLogger {

  private final CustomersService customersService;

  @LoggerName("customers")
  Logger log;

  @Inject
  @ConfigProperty(
    name = "application.customers-logger.startup.message",
    defaultValue = "Customers count: {0}"
  )
  String startupMessage;

  @Startup
  public void logCountOfCustomers() {
    var count = customersService.count();
    log.infov(this.startupMessage, count);
  }

}
