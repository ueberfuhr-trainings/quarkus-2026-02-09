package de.samples.quarkus.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Customer {

  @Null(groups = ValidationGroups.OnCreate.class)
  @NotNull(groups = ValidationGroups.OnUpdate.class)
  private UUID uuid;
  @Size(min = 3, max = 100)
  @NotNull
  private String name;
  @NotNull
  @ValidCustomerAge
  private LocalDate birthdate;
  private CustomerState state = CustomerState.ACTIVE;

}
