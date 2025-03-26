package io.github.llh4github.ksas.common.exceptions

import org.babyfish.jimmer.error.ErrorFamily
import org.babyfish.jimmer.error.ErrorField

@ErrorFamily
enum class UserModuleErrorCode {
    @ErrorField(name = "username", type = String::class)
    USERNAME_EXISTS,

    LOGIN_FAILED,

    JWT_INVALID,
}
