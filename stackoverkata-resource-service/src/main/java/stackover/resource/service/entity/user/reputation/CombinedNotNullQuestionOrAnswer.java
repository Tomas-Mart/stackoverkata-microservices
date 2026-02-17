package stackover.resource.service.entity.user.reputation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = ReputationValidator.class)
public @interface CombinedNotNullQuestionOrAnswer {
    String message() default "questionId or answerId is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}