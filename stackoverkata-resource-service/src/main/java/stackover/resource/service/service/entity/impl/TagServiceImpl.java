package stackover.resource.service.service.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.Tag;
import stackover.resource.service.repository.entity.TagRepository;
import stackover.resource.service.service.entity.TagService;

import java.util.List;

/**
 * Реактивная реализация сервиса для работы с тегами
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Flux<Tag> findOrCreateTagsByNames(List<String> tagNames) {
        log.debug("Поиск или создание тегов по именам: {}", tagNames);

        return Flux.fromIterable(tagNames)
                .flatMap(this::findOrCreateTag)
                .doOnNext(tag -> log.debug("Обработан тег: {}", tag.getName()))
                .doOnError(e -> log.error("Ошибка при обработке тегов", e));
    }

    private Mono<Tag> findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .switchIfEmpty(Mono.defer(() -> createNewTag(tagName)))
                .onErrorResume(e -> {
                    log.error("Ошибка при обработке тега: {}", tagName, e);
                    return Mono.error(e);
                });
    }

    private Mono<Tag> createNewTag(String tagName) {
        log.debug("Создание нового тега: {}", tagName);

        Tag newTag = new Tag();
        newTag.setName(tagName);
        newTag.setDescription("Описание тега по умолчанию");

        return tagRepository.save(newTag)
                .doOnSuccess(tag -> log.info("Создан новый тег: {}", tag))
                .doOnError(e -> log.error("Ошибка при создании тега", e));
    }
}