package io.github.llh4github.ksas.service.extra

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@SpringBootTest
class JwtServiceTest {
    @Autowired
    lateinit var service: JwtService

    @Test
    fun test_create_and_validate_jwt() {
        val userId = 114514L
        val token = service.createToken(userId) {
            mapOf("name" to "test")
        }
        val claims = service.parseToken(token)
        assertNotNull(claims)
        assertEquals(userId.toString(), claims?.subject)
        assertTrue(service.validateToken(token))
        service.removeToken(token)
        assertFalse(service.validateToken(token))
    }
}
