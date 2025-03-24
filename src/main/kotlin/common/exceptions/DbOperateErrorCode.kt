package io.github.llh4github.ksas.common.exceptions

import org.babyfish.jimmer.error.ErrorFamily

@ErrorFamily
enum class DbOperateErrorCode {
    /**
     * 新增数据失败
     */
    ADD_FAILED,

    /**
     * 更新数据失败
     */
    UPDATE_FAILED,

    /**
     * 删除数据失败
     */
    DELETE_FAILED,
}