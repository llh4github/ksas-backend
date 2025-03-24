package io.github.llh4github.ksas.common.exceptions

import org.babyfish.jimmer.error.ErrorFamily
import org.babyfish.jimmer.error.ErrorField

@ErrorFamily
enum class DbCommonErrorCode {
    /*
        * 数据已存在
    */
    @ErrorField(name = "fieldName", type = String::class)
    @ErrorField(name = "fieldValue", type = String::class)
    DATA_EXISTS,


    /*
     * 数据不存在
     */
    @ErrorField(name = "fieldName", type = String::class)
    @ErrorField(name = "fieldValue", type = String::class)
    DATA_NOT_EXISTS,
}