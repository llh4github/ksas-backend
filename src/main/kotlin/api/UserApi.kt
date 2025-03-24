package io.github.llh4github.ksas.api

import io.github.llh4github.ksas.dbmodel.auth.dto.UserPageEle
import io.github.llh4github.ksas.service.auth.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/auth/user")
class UserApi(
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    @Operation(summary = "根据ID获取用户")
    fun getById(@RequestParam(value = "id", required = true) id: Long): UserPageEle? {
        val dto = userService.getById(UserPageEle::class, id)
        logger.debug { "getById $dto" }
        return dto
    }

    /**
     * 接口响应速度差不多
     */
    @GetMapping("async")
    @Operation(summary = "根据ID获取用户(async)")
    suspend fun getByIdAsync(@RequestParam(value = "id", required = true) id: Long) =
        withContext(Dispatchers.IO) {
            val dto = userService.getByIdAsync(UserPageEle::class, id)
            logger.debug { "getByIdAsync $dto" }
            dto
        }
}
