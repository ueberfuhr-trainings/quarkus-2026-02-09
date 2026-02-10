package de.samples.quarkus;

import jakarta.json.bind.annotation.JsonbTransient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Customer {

  @Setter(onMethod_ = @JsonbTransient)
  private UUID uuid;
  private String name;
  private LocalDate birthdate;
  private String state;

}
