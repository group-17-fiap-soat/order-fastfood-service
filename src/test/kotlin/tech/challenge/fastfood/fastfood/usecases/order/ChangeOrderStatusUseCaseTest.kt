package tech.challenge.fastfood.fastfood.usecases.order

import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import tech.challenge.fastfood.fastfood.common.enums.OrderStatusEnum
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order
import java.util.*

class ChangeOrderStatusUseCaseTest {
    private val orderGateway: OrderGatewayInterface = mock()
    private val useCase = ChangeOrderStatusUseCase(orderGateway)

    @Test
    fun `should change status to provided status`() {
        val id = UUID.randomUUID()
        val order = Order(id, orderItems = emptyList(), status = OrderStatusEnum.RECEIVED)
        val updatedOrder = order.copy(status = OrderStatusEnum.READY)
        whenever(orderGateway.findById(id)).thenReturn(order)
        whenever(orderGateway.save(updatedOrder)).thenReturn(updatedOrder)

        val result = useCase.execute(id, OrderStatusEnum.READY)

        assertEquals(OrderStatusEnum.READY, result.status)
        verify(orderGateway).findById(id)
        verify(orderGateway).save(updatedOrder)
    }

    @Test
    fun `should change status to next status if status is null`() {
        val id = UUID.randomUUID()
        val order = Order(id, orderItems = emptyList(), status = OrderStatusEnum.RECEIVED)
        val nextStatus = OrderStatusEnum.IN_PROGRESS
        val updatedOrder = order.copy(status = nextStatus)
        whenever(orderGateway.findById(id)).thenReturn(order)
        whenever(orderGateway.save(updatedOrder)).thenReturn(updatedOrder)

        val result = useCase.execute(id, null)

        assertEquals(nextStatus, result.status)
        verify(orderGateway).findById(id)
        verify(orderGateway).save(updatedOrder)
    }

    @Test
    fun `should keep status if already last status and status is null`() {
        val id = UUID.randomUUID()
        val order = Order(id, orderItems = emptyList(), status = OrderStatusEnum.FINISHED)
        val updatedOrder = order.copy(status = OrderStatusEnum.FINISHED)
        whenever(orderGateway.findById(id)).thenReturn(order)
        whenever(orderGateway.save(updatedOrder)).thenReturn(updatedOrder)

        val result = useCase.execute(id, null)

        assertEquals(OrderStatusEnum.FINISHED, result.status)
        verify(orderGateway).findById(id)
        verify(orderGateway).save(updatedOrder)
    }

    @Test
    fun `should throw EntityNotFoundException if order not found`() {
        val id = UUID.randomUUID()
        whenever(orderGateway.findById(id)).thenReturn(null)

        val ex = assertThrows<EntityNotFoundException> { useCase.execute(id, OrderStatusEnum.READY) }
        assertTrue(ex.message!!.contains("Pedido com o id"))
        verify(orderGateway).findById(id)
    }
}

