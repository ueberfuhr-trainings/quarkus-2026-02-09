package de.samples.quarkus.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.METHOD,
  ElementType.FIELD,
  ElementType.PARAMETER,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.TYPE_USE
})
@Documented
// Bean Validation
@Constraint(validatedBy = {MinAgeValidator.class})
public @interface MinAge {

  long value();

  ChronoUnit unit() default ChronoUnit.YEARS;

  String message() default "Minimum age of {value} {unit} required";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}

