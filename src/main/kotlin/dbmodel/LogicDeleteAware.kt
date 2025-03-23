package io.github.llh4github.ksas.dbmodel

import org.babyfish.jimmer.sql.LogicalDeleted
import org.babyfish.jimmer.sql.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
interface LogicDeleteAware {
    @LogicalDeleted("now")
    val deletedTime: LocalDateTime?
}
