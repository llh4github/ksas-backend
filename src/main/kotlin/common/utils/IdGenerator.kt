package io.github.llh4github.ksas.common.utils

import com.github.yitter.contract.IdGeneratorOptions
import com.github.yitter.idgen.YitIdHelper
import io.github.llh4github.ksas.config.property.IdGeneratorProperty
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class IdGenerator(private val property: IdGeneratorProperty) {

    @PostConstruct
    fun postConstruct() {
        val options = IdGeneratorOptions(property.workerId)
        options.SeqBitLength = property.seqBitLength
        YitIdHelper.setIdGenerator(options)
    }

    fun nextId(): Long = YitIdHelper.nextId()

    fun nextIdStr(): String = nextId().toString()
}
