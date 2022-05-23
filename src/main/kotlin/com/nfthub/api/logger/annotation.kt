package com.nfthub.api.logger


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ClassAop

@Target(AnnotationTarget.FUNCTION)
annotation class MethodAop