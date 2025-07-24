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

class FindAllProductUseCaseTest {
    private val productGateway: ProductGatewayInterface = mock()
    private val useCase = FindAllProductUseCase(productGateway)

    @Test
    fun `should return all products when category is null`() {
        val products = listOf(Product(UUID.randomUUID(), "Coca", "",BigDecimal.TEN))
        whenever(productGateway.findAll()).thenReturn(products)

        val result = useCase.execute(null)

        assertEquals(products, result)
        verify(productGateway).findAll()
    }

    @Test
    fun `should return products by category when category is provided`() {
        val category = "Bebidas"
        val products = listOf(Product(UUID.randomUUID(), "Coca", "",BigDecimal.TEN))
        whenever(productGateway.findAllByCategory(category)).thenReturn(products)

        val result = useCase.execute(category)

        assertEquals(products, result)
        verify(productGateway).findAllByCategory(category)
    }
}

