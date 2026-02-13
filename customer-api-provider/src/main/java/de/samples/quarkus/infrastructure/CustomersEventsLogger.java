package de.samples.quarkus.infrastructure;

import de.samples.quarkus.domain.events.CustomerCreatedEvent;
import de.samples.quarkus.domain.events.CustomerUpdatedEvent;
import io.quarkus.arc.log.LoggerName;
import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

@IfBuildProperty(
  name = "application.customers-logger.enabled",
  stringValue = "true"
)
@ApplicationScoped
@RequiredArgsConstructor
public class CustomersEventsLogger {

  @LoggerName("customers")
  Logger log;

  public void logCustomerCreated(
    @Observes
    CustomerCreatedEvent event
  ) {
    log.infov("Customer created: {0}", event.customer().getUuid());
  }

  public void logCustomerUpdated(
    @Observes
    CustomerUpdatedEvent event
  ) {
    log.infov("Customer updated: {0}", event.customer().getUuid());
  }

}
