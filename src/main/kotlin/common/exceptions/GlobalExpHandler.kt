package io.github.llh4github.ksas.common.exceptions

import io.github.llh4github.ksas.common.req.JsonWrapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.babyfish.jimmer.error.CodeBasedRuntimeException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExpHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(CodeBasedRuntimeException::class)
    fun handleException(
        e: CodeBasedRuntimeException
    ): JsonWrapper<Map<String, Any?>> {
        return JsonWrapper.fail(
            data = e.fields, code = e.code,
            module = e.family,
            msg = e.message ?: "请求处理异常"
        )
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleException(e: RuntimeException): JsonWrapper<String> {
        logger.error(e) { "系统出现未知错误: ${e.message}" }
        return JsonWrapper.fail(data = e.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): JsonWrapper<String> {
        logger.error(e) { "系统出现未知错误: ${e.message}" }
        return JsonWrapper.fail(data = e.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(e: MethodArgumentNotValidException): JsonWrapper<Map<String, String?>> {
        logger.debug(e) { "接口参数校验失败" }
        val map = e.bindingResult.fieldErrors
            .filter { it.field.isNotEmpty() && it.defaultMessage != null }
            .associate { it.field to it.defaultMessage }
        return JsonWrapper.fail(
            data = map,
            code = "VALIDATION_ERROR",
            module = "API_PARAMS_VALIDATION",
            msg = "参数校验失败"
        )
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleException(e: NoResourceFoundException): JsonWrapper<String> {
        val path = e.resourcePath
        logger.error(e) { "访问不存在的资源: $path" }
        return JsonWrapper.fail(
            data = "资源不存在: $path",
            code = "NOT_FOUND", msg = "NO_RESOURCE"
        )
    }
}