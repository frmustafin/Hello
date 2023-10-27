package ru.otus.otuskotlin.marketplace.common.exceptions

import ru.otus.otuskotlin.marketplace.common.models.MkplProfileLock

class RepoConcurrencyException(expectedLock: MkplProfileLock, actualLock: MkplProfileLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)