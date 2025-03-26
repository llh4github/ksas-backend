package io.github.llh4github.ksas.payload.login

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length

@Schema(description = "User login view")
data class UserLoginView(
    @get:NotEmpty(message = "用户名不能为空")
    @get:Length(max = 50, message = "用户名长度必须在{min}-{max}个字符之间")
    @Schema(description = "Username", example = "Tom", required = true)
    val username: String,

    @get:Length(max = 50, message = "密码长度必须在{min}-{max}个字符之间")
    @get:NotEmpty(message = "密码不能为空")
    @Schema(description = "Password", example = "123456", required = true)
    val password: String
)

@Schema(description = "注销请求数据")
data class LogoutView(
    @get:NotEmpty(message = "访问凭证不能为空")
    @Schema(description = "访问凭证")
    val accessToken: String,

    @get:NotEmpty(message = "刷新凭证不能为空")
    @Schema(description = "刷新凭证")
    val refreshToken: String,
)

@Schema(description = "刷新jwt的请求数据")
data class RefreshJwtView(
    @Schema(description = "凭证")
    val accessToken: String,
    @Schema(description = "刷新凭证")
    val refreshToken: String,
)
