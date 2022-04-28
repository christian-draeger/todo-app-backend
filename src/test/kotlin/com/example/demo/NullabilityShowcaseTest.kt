package com.example.demo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NullabilityShowcaseTest {
    @Test
    internal fun `will throw Null pointer un unsafe calls`() {
        var name: String? = null
        assertThrows<NullPointerException> {
            name!!.length
        }
    }

    @Test
    internal fun `can call nullable type that has none null value`() {
        var name: String? = "chris"
        assertThat(name!!.length).isEqualTo(5)
    }

    @Test
    internal fun `will not call something on a null valued variable`() {
        var name: String? = null
        assertThat(name?.length).isEqualTo(null)
    }

    @Test
    internal fun `will call something on a nullable value that is not null`() {
        var name: String? = "chris"
        assertThat(name?.length).isEqualTo(5)
    }

    @Test
    internal fun `will fallback to a default value if null`() {
        var name: String? = null
        assertThat(name?.length ?: 1337).isEqualTo(1337)
    }
}