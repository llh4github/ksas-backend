package io.github.llh4github.ksas.dbmodel

import com.fasterxml.jackson.annotation.JsonFormat
import io.github.llh4github.ksas.common.consts.DatetimeConstant
import io.github.llh4github.ksas.dbmodel.auth.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import org.babyfish.jimmer.sql.*
import java.time.LocalDateTime

@MappedSuperclass
interface BaseModel {

    @Id
    @GeneratedValue(generatorType = TableIdGenerator::class)
    @get:Min(1, message = "id必须大于0")
    @get:Schema(title = "数据ID", example = "114514")
    val id: Long

    @Column(name = "created_time")
    @get:Schema(title = "创建时间", example = "2021-11-11 01:01:01")
    @get:JsonFormat(pattern = DatetimeConstant.DATE_TIME_FORMAT)
    val createdTime: LocalDateTime?

    @Column(name = "updated_time")
    @get:Schema(title = "更新时间", example = "2021-01-01 00:00:00")
    @get:JsonFormat(pattern = DatetimeConstant.DATE_TIME_FORMAT)
    val updatedTime: LocalDateTime?

    @ManyToOne
    @OnDissociate(DissociateAction.SET_NULL)
    val createdByUser: User?

    @ManyToOne
    @OnDissociate(DissociateAction.SET_NULL)
    val updatedByUser: User?
}
