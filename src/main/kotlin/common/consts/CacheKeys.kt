package io.github.llh4github.ksas.common.consts


object CacheKeys {

    private const val PREFIX = "biz-cache"

    const val USER_PERM_CODES = "${PREFIX}:user-permission-codes"

    fun userPermissionCodes(username: String): String {
        return "$PREFIX:user-permission-code:$username"
    }
}