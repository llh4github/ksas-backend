package io.github.llh4github.ksas.common.req

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "分页结果类")
data class PageResult<E>(
    @Schema(title = "数据总量")
    val totalRowCount: Long,
    @Schema(title = "总页数")
    val totalPage: Long,
    @Schema(title = "数据")
    val records: List<E>
)

@Schema(title = "分页参数")
interface PageQueryParamTrait {
    @get:Schema(title = "页码", example = "1")
    val pageIndex: Int

    @get:Schema(title = "页大小", example = "10")
    val pageSize: Int

    fun pageNum(): Int {
        return if (pageIndex < 1) pageIndex else pageIndex - 1
    }
}

@Schema(title = "默认分页参数类")
open class PageQueryParam : PageQueryParamTrait {

    @get:Schema(title = "页码", example = "1")
    override val pageIndex: Int = 1

    @Schema(title = "页大小", example = "10")
    override val pageSize: Int = 10
}
