package com.nfthub.core.mapper;

public interface RequestMapper<R, E> {
    public E fromEntity(R request);
}
