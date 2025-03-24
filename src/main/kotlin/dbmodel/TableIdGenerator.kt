package io.github.llh4github.ksas.dbmodel

import io.github.llh4github.ksas.common.utils.IdGenerator
import org.babyfish.jimmer.sql.meta.UserIdGenerator
import org.springframework.stereotype.Component

@Component
class TableIdGenerator(private val idGenerator: IdGenerator) : UserIdGenerator<Long> {

    override fun generate(entityType: Class<*>?): Long {
        return idGenerator.nextId()
    }
}
