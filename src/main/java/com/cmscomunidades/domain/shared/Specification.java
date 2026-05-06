package com.cmscomunidades.domain.shared;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);

    default Specification<T> and(Specification<T> other) {
        return t -> this.isSatisfiedBy(t) && other.isSatisfiedBy(t);
    }
}