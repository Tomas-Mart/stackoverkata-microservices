package stackover.resource.service.service.entity;

import reactor.core.publisher.Mono;

public interface AbstractService<T, PK> {

    Mono<T> findById(PK id);

    Mono<T> save(T entity);

    Mono<T> update(T entity);

    Mono<Void> deleteById(PK id);

    Mono<Boolean> existsById(PK id);

    Mono<T> saveWithFlush(T entity);
}