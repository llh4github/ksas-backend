package io.github.llh4github.ksas.service

import io.github.llh4github.ksas.dbmodel.BaseModel
import org.babyfish.jimmer.sql.kt.KSqlClient

interface SimpleUniqueDataOp<E : BaseModel> {

    /**
     * 检查唯一性，如果不唯一则抛出异常。
     * 只考虑主要字段的唯一性，其他字段的唯一性需要在具体的业务方法中处理
     */
    fun checkUnique(entity: E)


    fun addUniqueData(entity: E, sqlClient: KSqlClient): E {
        checkUnique(entity)
        val rs = sqlClient.insert(entity)
        testAddDbResult(rs)
        return rs.modifiedEntity
    }

    fun updateUniqueData(entity: E, sqlClient: KSqlClient): E {
        checkUnique(entity)
        val rs = sqlClient.update(entity)
        testUpdateDbResult(rs)
        return rs.modifiedEntity
    }
}
