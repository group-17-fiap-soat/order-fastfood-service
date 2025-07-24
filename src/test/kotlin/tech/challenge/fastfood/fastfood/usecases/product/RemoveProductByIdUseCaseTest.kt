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

class RemoveProductByIdUseCaseTest {
    private val productGateway: ProductGatewayInterface = mock()
    private val useCase = RemoveProductByIdUseCase(productGateway)

    @Test
    fun `should remove product when found`() {
        val id = UUID.randomUUID()
        val product = Product(id, "Coca", "", BigDecimal.TEN)
        whenever(productGateway.findById(id)).thenReturn(product)

        useCase.execute(id)

        verify(productGateway).findById(id)
        verify(productGateway).delete(id)
    }

    @Test
    fun `should throw EntityNotFoundException when product not found`() {
        val id = UUID.randomUUID()
        whenever(productGateway.findById(id)).thenReturn(null)

        val ex = assertThrows<EntityNotFoundException> { useCase.execute(id) }
        assertEquals("A entidade com o id fornecido não existe, revise o corpo da requisição", ex.message)
        verify(productGateway).findById(id)
    }
}

