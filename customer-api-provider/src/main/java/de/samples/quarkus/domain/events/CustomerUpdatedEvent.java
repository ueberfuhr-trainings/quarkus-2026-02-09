package de.samples.quarkus.domain.events;

import de.samples.quarkus.domain.Customer;

public record CustomerUpdatedEvent(
  Customer customer
) implements CustomerEvent {
}
