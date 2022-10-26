package com.nfthub.core.mapper;

public interface ResponseMapper<E, R> {
    R toResponse(E entity);
}

