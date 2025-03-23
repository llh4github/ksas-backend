package io.github.llh4github.ksas.dbmodel

import io.github.llh4github.ksas.common.utils.IdGenerator
import org.babyfish.jimmer.sql.meta.UserIdGenerator

class TableIdGenerator : UserIdGenerator<Long> {

    override fun generate(entityType: Class<*>?): Long {
        return IdGenerator.nextId()
    }
}
