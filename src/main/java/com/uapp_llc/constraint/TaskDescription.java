package com.uapp_llc.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Size(min = 1, max = 100)
@Constraint(validatedBy = {})
public @interface TaskDescription {

  String message() default "invalid task description";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
