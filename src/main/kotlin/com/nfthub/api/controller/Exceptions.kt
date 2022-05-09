package com.nfthub.api.controller

class NotFoundException(message: String) : RuntimeException(message)
class AlreadyExistException(message: String) : RuntimeException(message)
class UnAuthenticatedException(message: String) : RuntimeException(message)
class ForbiddenException(message: String) : RuntimeException(message)
class BadRequestException(message: String) : RuntimeException(message)
class ConflictException(message: String) : RuntimeException(message)

// user
class UserEmailExistException(message: String) : RuntimeException(message)

