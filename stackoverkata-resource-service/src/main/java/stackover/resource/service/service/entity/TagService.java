package stackover.resource.service.service.entity;

import reactor.core.publisher.Flux;
import stackover.resource.service.entity.question.Tag;

import java.util.List;

public interface TagService {

    Flux<Tag> findOrCreateTagsByNames(List<String> tagNames);
}