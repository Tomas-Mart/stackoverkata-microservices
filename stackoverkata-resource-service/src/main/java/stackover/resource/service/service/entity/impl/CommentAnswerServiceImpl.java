package stackover.resource.service.service.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.answer.Answer;
import stackover.resource.service.entity.question.answer.CommentAnswer;
import stackover.resource.service.entity.user.User;
import stackover.resource.service.exception.AccountNotAvailableException;
import stackover.resource.service.exception.AnswerException;
import stackover.resource.service.exception.UserNotFoundException;
import stackover.resource.service.feign.AuthServiceClient;
import stackover.resource.service.repository.entity.CommentAnswerRepository;
import stackover.resource.service.service.entity.AnswerService;
import stackover.resource.service.service.entity.CommentAnswerService;
import stackover.resource.service.service.entity.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentAnswerServiceImpl implements CommentAnswerService {

    private final UserService userService;
    private final AnswerService answerService;
    private final AuthServiceClient authServiceClient;
    private final CommentAnswerRepository commentAnswerRepository;

    @Override
    public Mono<CommentAnswer> addCommentToAnswer(Long answerId, Long accountId, String text) {
        return validateAccount(accountId)
                .then(validateUserAndAnswer(accountId, answerId))
                .flatMap(tuple -> createAndSaveComment(text, tuple.getT1(), tuple.getT2()))
                .doOnSuccess(comment -> log.info("Comment added successfully: {}", comment.getId()))
                .doOnError(e -> log.error("Failed to add comment: {}", e.getMessage()));
    }

    private Mono<Long> validateAccount(Long accountId) {
        return authServiceClient.isAccountExist(accountId)
                .handle((exists, sink) -> {
                    if (Boolean.TRUE.equals(exists)) {
                        sink.next(accountId);
                    } else {
                        sink.error(new AccountNotAvailableException("Account not found"));
                    }
                });
    }

    private Mono<User> validateUser(Long accountId) {
        return userService.getUserByAccountId(accountId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")));
    }

    private Mono<Answer> validateAnswer(Long answerId) {
        return answerService.findById(answerId)
                .switchIfEmpty(Mono.error(new AnswerException("Answer not found")));
    }

    private Mono<reactor.util.function.Tuple2<User, Answer>> validateUserAndAnswer(Long accountId, Long answerId) {
        return Mono.zip(
                validateUser(accountId),
                validateAnswer(answerId)
        );
    }

    private Mono<CommentAnswer> createAndSaveComment(String text, User user, Answer answer) {
        CommentAnswer comment = new CommentAnswer(text, user);
        comment.setAnswer(answer);
        return commentAnswerRepository.save(comment);
    }
}