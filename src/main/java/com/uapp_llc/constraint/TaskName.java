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
@Size(min = 1, max = 50)
@javax.validation.constraints.Email
@Constraint(validatedBy = {})
public @interface TaskName {

  String message() default "invalid task name";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
