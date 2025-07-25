package tech.challenge.fastfood.fastfood.usecases.product

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.ProductGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Product
import java.math.BigDecimal
import java.util.*

class FindProductByIdUseCaseBddTest {

    private lateinit var productGateway: ProductGatewayInterface
    private lateinit var findProductByIdUseCase: FindProductByIdUseCase

    @BeforeEach
    fun setUp() {
        productGateway = mock()
        findProductByIdUseCase = FindProductByIdUseCase(productGateway)
    }

    @Test
    fun `should return product when product exists`() {
        // Given (Dado que): Um produto com um ID específico existe no sistema.
        // Preparamos o mock para simular que o produto foi encontrado no "banco de dados".
        val productId = UUID.randomUUID()
        val expectedProduct = Product(id = productId, name = "Cheeseburger", description = "Classic cheeseburger", price = BigDecimal("9.99"))
        whenever(productGateway.findById(productId)).thenReturn(expectedProduct)

        // When (Quando): O caso de uso é executado para buscar o produto com esse ID.
        // Esta é a ação que estamos testando.
        val actualProduct = findProductByIdUseCase.execute(productId)

        // Then (Então): O produto correspondente deve ser retornado.
        // Verificamos se o resultado não é nulo e se é exatamente o produto que esperávamos.
        assertNotNull(actualProduct)
        assertEquals(expectedProduct, actualProduct)
    }

    @Test
    fun `should return null when product does not exist`() {
        // Given (Dado que): Um ID de produto é fornecido, mas ele não corresponde a nenhum produto no sistema.
        // Preparamos o mock para simular que nada foi encontrado no "banco de dados".
        val productId = UUID.randomUUID()
        whenever(productGateway.findById(productId)).thenReturn(null)

        // When (Quando): O caso de uso é executado para buscar o produto com esse ID.
        // A ação é a mesma, mas o contexto (Given) é diferente.
        val actualProduct = findProductByIdUseCase.execute(productId)

        // Then (Então): O resultado da busca deve ser nulo.
        // Verificamos se o resultado é nulo, indicando que o produto não foi encontrado.
        assertNull(actualProduct)
    }
}