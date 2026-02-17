package stackover.resource.service.entity.user.reputation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ReputationValidator implements ConstraintValidator<CombinedNotNullQuestionOrAnswer, Reputation> {

    @Override
    public void initialize(final CombinedNotNullQuestionOrAnswer combinedNotNull) {
    }

    @Override
    public boolean isValid(final Reputation reputation, final ConstraintValidatorContext context) {

        return (reputation.getQuestion() == null && reputation.getAnswer() != null)
                && (reputation.getType() == ReputationType.ANSWER || reputation.getType() == ReputationType.VOTE_ANSWER)
                || (reputation.getQuestion() != null && reputation.getAnswer() == null)
                && (reputation.getType() == ReputationType.QUESTION || reputation.getType() == ReputationType.VOTE_QUESTION);
    }
}
