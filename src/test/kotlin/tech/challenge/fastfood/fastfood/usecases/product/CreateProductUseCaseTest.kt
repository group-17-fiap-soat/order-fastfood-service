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

class CreateProductUseCaseTest {
    private val productGateway: ProductGatewayInterface = mock()
    private val useCase = CreateProductUseCase(productGateway)

    @Test
    fun `should create and return product`() {
        val product = Product(UUID.randomUUID(), "Coca", "",BigDecimal.TEN)
        whenever(productGateway.save(product)).thenReturn(product)

        val result = useCase.execute(product)

        assertEquals(product, result)
        verify(productGateway).save(product)
    }
}

