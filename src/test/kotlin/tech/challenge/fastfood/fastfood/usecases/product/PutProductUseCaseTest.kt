package tech.challenge.fastfood.fastfood.usecases.product

import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.ProductGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Product
import java.math.BigDecimal
import java.util.*

class PutProductUseCaseTest {
    private val productGateway: ProductGatewayInterface = mock()
    private val useCase = PutProductUseCase(productGateway)

    @Test
    fun `should update and return product when found`() {
        val id = UUID.randomUUID()
        val product = Product(id, "Coca", "",BigDecimal.TEN)
        whenever(productGateway.findById(id)).thenReturn(product)
        whenever(productGateway.save(product)).thenReturn(product)

        val result = useCase.execute(product)

        assertEquals(product, result)
        verify(productGateway).findById(id)
        verify(productGateway).save(product)
    }

    @Test
    fun `should throw exception when id is null`() {
        val product = Product(null, "Coca", "",BigDecimal.TEN)
        val ex = assertThrows<IllegalArgumentException> { useCase.execute(product) }
        assertEquals("O id do produto não pode ser nulo", ex.message)
    }

    @Test
    fun `should throw EntityNotFoundException when product not found`() {
        val id = UUID.randomUUID()
        val product = Product(id, "Coca","", BigDecimal.TEN)
        whenever(productGateway.findById(id)).thenReturn(null)

        val ex = assertThrows<EntityNotFoundException> { useCase.execute(product) }
        assertEquals("A entidade com o id fornecido não existe, revise o corpo da requisição", ex.message)
        verify(productGateway).findById(id)
    }
}

