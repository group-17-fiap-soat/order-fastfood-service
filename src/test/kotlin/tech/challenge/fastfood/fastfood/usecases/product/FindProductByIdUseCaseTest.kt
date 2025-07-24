package tech.challenge.fastfood.fastfood.usecases.product

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.ProductGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Product
import java.math.BigDecimal
import java.util.*

class FindProductByIdUseCaseTest {
    private val productGateway: ProductGatewayInterface = mock()
    private val useCase = FindProductByIdUseCase(productGateway)

    @Test
    fun `should return product when found`() {
        val id = UUID.randomUUID()
        val product = Product(id, "Coca", "",BigDecimal.TEN)
        whenever(productGateway.findById(id)).thenReturn(product)

        val result = useCase.execute(id)

        assertEquals(product, result)
        verify(productGateway).findById(id)
    }

    @Test
    fun `should return null when product not found`() {
        val id = UUID.randomUUID()
        whenever(productGateway.findById(id)).thenReturn(null)

        val result = useCase.execute(id)

        assertNull(result)
        verify(productGateway).findById(id)
    }
}

