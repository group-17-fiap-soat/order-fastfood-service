package tech.challenge.fastfood.fastfood.usecases.order

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import tech.challenge.fastfood.fastfood.adapters.clients.PaymentApiClient
import tech.challenge.fastfood.fastfood.common.enums.OrderStatusEnum
import tech.challenge.fastfood.fastfood.common.enums.PaymentStatusEnum
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderItemGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order
import tech.challenge.fastfood.fastfood.entities.OrderItem
import tech.challenge.fastfood.fastfood.entities.PaymentAssociation
import tech.challenge.fastfood.fastfood.entities.PaymentData
import tech.challenge.fastfood.fastfood.entities.Product
import java.math.BigDecimal
import java.util.*

class ListOrderUseCaseTest {
    private val orderGateway: OrderGatewayInterface = mock()
    private val orderItemGateway: OrderItemGatewayInterface = mock()
    private val paymentApiClient: PaymentApiClient = mock()
    private val useCase = ListOrderUseCase(orderGateway, orderItemGateway, paymentApiClient)

    @Test
    fun `deve retornar pedidos com itens e pagamento, exceto finalizados, ordenados por prioridade e data`() {
        val orderId1 = UUID.randomUUID()
        val orderId2 = UUID.randomUUID()
        val order1 = Order(orderId1, status = OrderStatusEnum.PENDING_AUTHORIZATION, orderItems = emptyList())
        val order2 = Order(orderId2, status = OrderStatusEnum.IN_PROGRESS, orderItems = emptyList())
        val order3 = Order(UUID.randomUUID(), status = OrderStatusEnum.FINISHED, orderItems = emptyList())
        val orderItem1 = OrderItem(UUID.randomUUID(), Product(UUID.randomUUID(), "Coca", "", BigDecimal.TEN), 1, orderId1)
        val orderItem2 = OrderItem(UUID.randomUUID(), Product(UUID.randomUUID(), "Batata", "", BigDecimal.ONE), 2, orderId2)
        val payment1 = PaymentData(UUID.randomUUID(), 1L, orderId1, BigDecimal.TEN, "PIX", paymentStatus = PaymentStatusEnum.PAYMENT_APPROVED)
        val payment2 = PaymentData(UUID.randomUUID(), 2L, orderId2, BigDecimal.ONE, "PIX", paymentStatus = PaymentStatusEnum.PAYMENT_APPROVED)
        whenever(orderGateway.findAll()).thenReturn(listOf(order1, order2, order3))
        whenever(orderItemGateway.findAllByOrderId(orderId1)).thenReturn(listOf(orderItem1))
        whenever(orderItemGateway.findAllByOrderId(orderId2)).thenReturn(listOf(orderItem2))
        whenever(paymentApiClient.getPaymentByOrderId(orderId1)).thenReturn(listOf(payment1))
        whenever(paymentApiClient.getPaymentByOrderId(orderId2)).thenReturn(listOf(payment2))

        val result = useCase.execute()

        assertEquals(2, result.size)
        assertEquals(OrderStatusEnum.IN_PROGRESS, result[0].status)
        assertEquals(OrderStatusEnum.PENDING_AUTHORIZATION, result[1].status)
        assertEquals(listOf(orderItem2), result[0].orderItems)
        assertEquals(listOf(orderItem1), result[1].orderItems)
        assertEquals(payment2, result[0].payment?.paymentData)
        assertEquals(payment1, result[1].payment?.paymentData)
        assertTrue(result.none { it.status == OrderStatusEnum.FINISHED })
        verify(orderGateway).findAll()
        verify(orderItemGateway).findAllByOrderId(orderId1)
        verify(orderItemGateway).findAllByOrderId(orderId2)
        verify(paymentApiClient).getPaymentByOrderId(orderId1)
        verify(paymentApiClient).getPaymentByOrderId(orderId2)
    }

    @Test
    fun `deve retornar lista vazia quando não houver pedidos`() {
        whenever(orderGateway.findAll()).thenReturn(emptyList())
        val result = useCase.execute()
        assertTrue(result.isEmpty())
        verify(orderGateway).findAll()
    }

    @Test
    fun `deve lidar com pagamento ausente sem lançar exceção`() {
        val orderId = UUID.randomUUID()
        val order = Order(orderId, status = OrderStatusEnum.PENDING_AUTHORIZATION, orderItems = emptyList())
        val orderItem = OrderItem(UUID.randomUUID(), Product(UUID.randomUUID(), "Coca", "", BigDecimal.TEN), 1, orderId)
        whenever(orderGateway.findAll()).thenReturn(listOf(order))
        whenever(orderItemGateway.findAllByOrderId(orderId)).thenReturn(listOf(orderItem))
        whenever(paymentApiClient.getPaymentByOrderId(orderId)).thenReturn(emptyList())

        val result = useCase.execute()
        assertEquals(1, result.size)
        assertNull(result[0].payment)
        verify(orderGateway).findAll()
        verify(orderItemGateway).findAllByOrderId(orderId)
        verify(paymentApiClient).getPaymentByOrderId(orderId)
    }
} 