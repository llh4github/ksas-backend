package io.github.llh4github.ksas.payload.login

import com.fasterxml.jackson.annotation.JsonFormat
import io.github.llh4github.ksas.common.consts.DatetimeConstant
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*


data class LoginOkToken(
    @Schema(description = "用户ID")
    val userId: Long,
    @Schema(description = "用户名")
    val username: String,
    @Schema(description = "访问凭证")
    val accessToken: String,
    @Schema(description = "刷新凭证")
    val refreshToken: String,
    /**
     * 访问凭证过期时间。减少前端计算
     */
    @Schema(description = "访问凭证过期时间")
    @JsonFormat(pattern = DatetimeConstant.DATE_TIME_FORMAT)
    val expire: Date,
)
