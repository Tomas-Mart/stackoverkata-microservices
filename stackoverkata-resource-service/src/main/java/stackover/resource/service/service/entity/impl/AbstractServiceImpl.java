package stackover.resource.service.service.entity.impl;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import stackover.resource.service.service.entity.AbstractService;

public abstract class AbstractServiceImpl<T, PK, R extends ReactiveCrudRepository<T, PK>>
        implements AbstractService<T, PK> {

    protected final R repository;

    protected AbstractServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public Mono<T> findById(PK id) {
        return repository.findById(id);
    }

    @Override
    public Mono<T> save(T entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<T> update(T entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Void> deleteById(PK id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(PK id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<T> saveWithFlush(T entity) {
        return repository.save(entity)
                .then(repository.findById(getId(entity)))
                .flatMap(repository::save);
    }

    protected abstract PK getId(T entity);
}