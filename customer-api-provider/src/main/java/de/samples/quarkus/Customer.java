package de.samples.quarkus;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "Customer") // Name im JQPL
@Table(name = "CUSTOMERS")
public class Customer {

  @Setter(onMethod_ = @JsonbTransient)
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;
  @Size(min = 3, max = 100)
  @NotNull
  private String name;
  @NotNull
  @MinAge(14)
  @Column(name = "BIRTH_DATE")
  private LocalDate birthdate;
  @ValidState
  private String state;

}
