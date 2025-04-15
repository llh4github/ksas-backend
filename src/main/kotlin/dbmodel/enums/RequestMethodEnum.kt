package io.github.llh4github.ksas.dbmodel.enums

import org.babyfish.jimmer.sql.EnumType

/**
 * 请求方法枚举
 */
@EnumType(EnumType.Strategy.NAME)
enum class RequestMethodEnum {
    /**
     * 所有
     */
    ALL,
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS,
    TRACE,
    ;

    fun match(method: String): Boolean {
        return this == ALL || this.name == method.uppercase()
    }
}
