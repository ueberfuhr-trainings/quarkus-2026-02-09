package de.samples.quarkus.domain.events;

import de.samples.quarkus.domain.Customer;

public record CustomerCreatedEvent(
  Customer customer
) implements CustomerEvent {
}
