 package tech.challenge.fastfood.fastfood.usecases.order

import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import tech.challenge.fastfood.fastfood.adapters.clients.PaymentApiClient
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderItemGatewayInterface
import tech.challenge.fastfood.fastfood.entities.*
import java.util.*
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever

class GetOrderByIdUseCaseTest {
    private val orderGateway: OrderGatewayInterface = mock()
    private val orderItemGateway: OrderItemGatewayInterface = mock()
    private val paymentApiClient: PaymentApiClient = mock()
    private val useCase = GetOrderByIdUseCase(orderGateway, orderItemGateway, paymentApiClient)

    private val orderId = UUID.randomUUID()
    private val orderItemId = UUID.randomUUID()
    private val paymentId = UUID.randomUUID()
    private val productId = UUID.randomUUID()

    private fun fakeOrder() = Order(
        id = orderId,
        orderItems = listOf(),
        status = null
    )
    private fun fakeOrderItem() = OrderItem(
        id = orderItemId,
        product = Product(id = productId, name = "Burger", price = java.math.BigDecimal.TEN),
        quantity = 1,
        orderId = orderId
    )
    private fun fakePaymentData() = PaymentData(
        id = paymentId,
        orderId = orderId,
        totalAmount = java.math.BigDecimal.TEN,
        paymentMethod = "PIX",
        paymentStatus = tech.challenge.fastfood.fastfood.common.enums.PaymentStatusEnum.PAYMENT_APPROVED
    )

    @Test
    fun `should return order with items and payment when found`() {
        val order = fakeOrder()
        val orderItems = listOf(fakeOrderItem())
        val paymentData = fakePaymentData()
        val paymentAssociation = PaymentAssociation(paymentData = paymentData)

        whenever(orderGateway.findById(orderId)).thenReturn(order)
        whenever(orderItemGateway.findAllByOrderId(orderId)).thenReturn(orderItems)
        whenever(paymentApiClient.getPaymentByOrderId(orderId)).thenReturn(listOf(paymentAssociation))

        val result = useCase.execute(orderId)

        assertEquals(orderId, result.id)
        assertEquals(orderItems, result.orderItems)
        assertNotNull(result.payment)
        assertEquals(paymentData, result.payment?.paymentData)
        verify(orderGateway).findById(orderId)
        verify(orderItemGateway).findAllByOrderId(orderId)
        verify(paymentApiClient).getPaymentByOrderId(orderId)
    }

    @Test
    fun `should throw EntityNotFoundException when order not found`() {
        whenever(orderGateway.findById(orderId)).thenReturn(null)
        val ex = assertThrows<EntityNotFoundException> {
            useCase.execute(orderId)
        }
        assertTrue(ex.message!!.contains("Pedido com o id"))
        verify(orderGateway).findById(orderId)
        verify(orderItemGateway, never()).findAllByOrderId(any())
        verify(paymentApiClient, never()).getPaymentByOrderId(any())
    }
}
