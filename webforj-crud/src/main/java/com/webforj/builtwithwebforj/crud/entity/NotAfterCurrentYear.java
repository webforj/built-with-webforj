package com.webforj.builtwithwebforj.crud.entity;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Year;

/**
 * The year must not be later than the current year. A fixed {@code @Max} would go stale every
 * January.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotAfterCurrentYear.Validator.class)
public @interface NotAfterCurrentYear {

  String message() default "Year must be current year or earlier";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Compares the year with the current one. An empty value is left to {@code @NotNull}.
   */
  class Validator implements ConstraintValidator<NotAfterCurrentYear, Integer> {

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
      return year == null || year <= Year.now().getValue();
    }
  }
}
