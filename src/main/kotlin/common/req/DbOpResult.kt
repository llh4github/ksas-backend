package io.github.llh4github.ksas.common.req

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "数据库操作结果")
data class DbOpResult(
    @get:Schema(title = "操作是否成功")
    val success: Boolean,
    @get:Schema(title = "操作结果描述")
    val message: String? = null,
) {
    companion object {
        fun success(message: String? = null): DbOpResult {
            return DbOpResult(true, message)
        }

        fun failure(message: String? = null): DbOpResult {
            return DbOpResult(false, message)
        }
    }
}
