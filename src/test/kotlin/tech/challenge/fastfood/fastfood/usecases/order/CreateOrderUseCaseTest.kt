package tech.challenge.fastfood.fastfood.usecases.order

import org.apache.coyote.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import tech.challenge.fastfood.fastfood.adapters.clients.PaymentApiClient
import tech.challenge.fastfood.fastfood.common.enums.OrderStatusEnum
import tech.challenge.fastfood.fastfood.common.enums.PaymentStatusEnum
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderItemGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.ProductGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order
import tech.challenge.fastfood.fastfood.entities.OrderItem
import tech.challenge.fastfood.fastfood.entities.PaymentAssociation
import tech.challenge.fastfood.fastfood.entities.PaymentData
import tech.challenge.fastfood.fastfood.entities.Product
import java.math.BigDecimal
import java.util.*

class CreateOrderUseCaseTest {
    private val orderGateway: OrderGatewayInterface = mock()
    private val orderItemGateway: OrderItemGatewayInterface = mock()
    private val productGateway: ProductGatewayInterface = mock()
    private val paymentApiClient: PaymentApiClient = mock()
    private val useCase = CreateOrderUseCase(orderGateway, orderItemGateway, productGateway, paymentApiClient)

    @Test
    fun `should create order with items and payment`() {
        val productId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val orderItemId = UUID.randomUUID()
        val product = Product(productId, "Coca", "", BigDecimal.TEN)
        val orderItem = OrderItem(orderItemId, product, 1, null)
        val order = Order(orderId, orderItems = listOf(orderItem))
        val savedOrder = order.copy(id = orderId, status = OrderStatusEnum.PENDING_AUTHORIZATION)
        val savedOrderItem = orderItem.copy(orderId = orderId, id = orderItemId)
        val payment = PaymentData(
            UUID.randomUUID(),
            1L,
            orderId,
            BigDecimal.TEN,
            "PIX",
            paymentStatus = PaymentStatusEnum.PAYMENT_APPROVED
        )
        val paymentAssociation = PaymentAssociation(payment)
        whenever(orderGateway.save(any())).thenReturn(savedOrder)
        whenever(orderItemGateway.saveAll(any())).thenReturn(listOf(savedOrderItem))
        whenever(productGateway.findByOrderItemId(orderItemId)).thenReturn(product)
        whenever(paymentApiClient.createPaymentAssociation(any())).thenReturn(paymentAssociation)

        val result = useCase.execute(order)

        assertEquals(savedOrder.id, result.id)
        assertEquals(OrderStatusEnum.PENDING_AUTHORIZATION, result.status)
        assertEquals(1, result.orderItems.size)
        assertEquals(product, result.orderItems[0].product)
        assertEquals(payment, result.payment?.paymentData)
        verify(orderGateway).save(any())
        verify(orderItemGateway).saveAll(any())
        verify(productGateway).findByOrderItemId(orderItemId)
        verify(paymentApiClient).createPaymentAssociation(any())
    }

    @Test
    fun `should throw BadRequestException if order has no items`() {
        val order = Order(UUID.randomUUID(), orderItems = emptyList())
        val ex = assertThrows<BadRequestException> { useCase.execute(order) }
        assertTrue(ex.message!!.contains("O pedido deve conter produtos"))
    }

    @Test
    fun `should throw BadRequestException if order has duplicated products`() {
        val productId = UUID.randomUUID()
        val product = Product(productId, "Coca", "", BigDecimal.TEN)
        val orderItem1 = OrderItem(UUID.randomUUID(), product, 1, null)
        val orderItem2 = OrderItem(UUID.randomUUID(), product, 2, null)
        val order = Order(UUID.randomUUID(), orderItems = listOf(orderItem1, orderItem2))
        val ex = assertThrows<BadRequestException> { useCase.execute(order) }
        assertTrue(ex.message!!.contains("Produto duplicado"))
    }
}

