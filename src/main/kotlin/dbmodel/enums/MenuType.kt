package io.github.llh4github.ksas.dbmodel.enums

import org.babyfish.jimmer.sql.EnumType

/**
 * 定义顺序很重要
 * 0代表菜单、1代表iframe、2代表外链、3代表按钮
 */
@EnumType(EnumType.Strategy.ORDINAL)
enum class MenuType {

    /**
     * 菜单
     */
    MENU,

    /**
     * iframe
     */
    IFRAME,

    /**
     * 外链
     */
    LINK,

    /**
     * 按钮
     */
    BUTTON
}
