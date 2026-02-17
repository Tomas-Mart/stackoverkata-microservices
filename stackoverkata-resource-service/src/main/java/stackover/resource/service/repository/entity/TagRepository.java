package stackover.resource.service.repository.entity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import stackover.resource.service.entity.question.Tag;

/**
 * Реактивный репозиторий для работы с сущностью Tag
 */
public interface TagRepository extends ReactiveCrudRepository<Tag, Long> {

    /**
     * Найти тег по имени
     *
     * @param name имя тега
     * @return Mono с найденным тегом или empty, если не найден
     */
    Mono<Tag> findByName(String name);
}