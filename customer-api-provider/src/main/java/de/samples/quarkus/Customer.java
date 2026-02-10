package de.samples.quarkus;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Customer {

  @Setter(onMethod_ = @JsonbTransient)
  private UUID uuid;
  @Size(min = 3, max = 100)
  @NotNull
  private String name;
  @NotNull
  @MinAge(14)
  private LocalDate birthdate;
  @ValidState
  private String state;

}
