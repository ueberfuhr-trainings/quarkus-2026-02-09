package de.samples.quarkus.domain;

import de.samples.quarkus.shared.validation.MinAge;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Constraint(validatedBy = {})
@MinAge(14)
@ReportAsSingleViolation
public @interface ValidCustomerAge {

  String message() default "Customer must be at least 14 years old.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
