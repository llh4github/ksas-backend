package io.github.llh4github.ksas.service.extra

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test


@SpringBootTest
class JwtServiceTest {
    @Autowired
    lateinit var service: JwtService

    @Test
    fun test_create_and_validate_jwt() {
        val userId = 114514L
        val token = service.createToken(userId)
        val claims = service.validateToken(token)
        assertNotNull(claims)
        assertEquals(userId.toString(), claims?.subject)
    }
}
