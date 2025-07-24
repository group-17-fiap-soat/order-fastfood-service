package tech.challenge.fastfood.fastfood.usecases.order

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderItemGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order
import tech.challenge.fastfood.fastfood.entities.OrderItem
import tech.challenge.fastfood.fastfood.entities.Product
import java.math.BigDecimal
import java.util.*

class ListOrderByIdUseCaseTest {
    private val orderGateway: OrderGatewayInterface = mock()
    private val orderItemGateway: OrderItemGatewayInterface = mock()
    private val useCase = ListOrderByIdUseCase(orderGateway, orderItemGateway)

    @Test
    fun `should return orders with their items`() {
        val orderId = UUID.randomUUID()
        val order = Order(orderId, orderItems = emptyList())
        val orderItem = OrderItem(UUID.randomUUID(), Product(UUID.randomUUID(), "Coca", "",BigDecimal.TEN), 1, orderId)
        whenever(orderGateway.findAll()).thenReturn(listOf(order))
        whenever(orderItemGateway.findAllByOrderId(orderId)).thenReturn(listOf(orderItem))

        val result = useCase.execute() ?: emptyList()

        assertEquals(1, result.size)
        assertEquals(listOf(orderItem), result[0].orderItems)
        verify(orderGateway).findAll()
        verify(orderItemGateway).findAllByOrderId(orderId)
    }

    @Test
    fun `should return empty list when no orders`() {
        whenever(orderGateway.findAll()).thenReturn(emptyList())
        val result = useCase.execute() ?: emptyList()
        assertTrue(result.isEmpty())
        verify(orderGateway).findAll()
    }

    @Test
    fun `should return orders with empty items if none found`() {
        val orderId = UUID.randomUUID()
        val order = Order(orderId, orderItems = emptyList(), )
        whenever(orderGateway.findAll()).thenReturn(listOf(order))
        whenever(orderItemGateway.findAllByOrderId(orderId)).thenReturn(emptyList())

        val result = useCase.execute() ?: emptyList()
        assertEquals(1, result.size)
        assertTrue(result[0].orderItems.isEmpty())
        verify(orderGateway).findAll()
        verify(orderItemGateway).findAllByOrderId(orderId)
    }
}
