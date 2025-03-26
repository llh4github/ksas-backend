package io.github.llh4github.ksas.service

import io.github.llh4github.ksas.common.exceptions.DbOperateException
import org.babyfish.jimmer.sql.kt.ast.mutation.KDeleteResult
import org.babyfish.jimmer.sql.kt.ast.mutation.KSimpleSaveResult


/**
 * 检查新增数据结果. 如果新增数据失败, 则抛出异常
 * @throws DbOperateException 新增数据失败
 */
internal fun testAddDbResult(
    rs: KSimpleSaveResult<*>,
    message: String = "新增数据失败"
) {
    if (!rs.isModified) {
        throw DbOperateException.addFailed(message = message)
    }
}

/**
 * 检查更新数据结果. 如果更新数据失败, 则抛出异常
 * @throws DbOperateException 更新数据失败
 */
internal fun testUpdateDbResult(
    rs: KSimpleSaveResult<*>,
    message: String = "更新数据失败"
) {
    if (!rs.isModified) {
        throw DbOperateException.updateFailed(message = message)
    }
}

/**
 * 检查删除数据结果. 如果删除数据失败, 则抛出异常
 * @throws DbOperateException 删除数据失败
 */
internal fun testDeleteDbResult(
    rs: KDeleteResult,
    message: String = "没有数据被删除"
) {
    if (rs.totalAffectedRowCount == 0) {
        throw DbOperateException.deleteFailed(message = message)
    }
}
