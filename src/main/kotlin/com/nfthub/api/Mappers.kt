package com.nfthub.api

interface ResponseMapper<R, E> {
    fun fromEntity(entity: E): R
}

interface RequestMapper<R, E> {
    fun toEntity(request: R): E
}


