package de.samples.quarkus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Optional;

public class MinAgeValidator
  implements ConstraintValidator<MinAge, LocalDate> {

  private MinAge annotation;

  @Override
  public void initialize(MinAge constraintAnnotation) {
    this.annotation = constraintAnnotation;
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    return Optional
      .ofNullable(value)
      .map(
        v -> !v.isAfter(
          LocalDate
            .now()
            .minus(annotation.value(), annotation.unit())
        )
      )
      .orElse(true);
  }
}
