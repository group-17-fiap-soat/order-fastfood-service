package tech.challenge.fastfood.fastfood.usecases.order

import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import tech.challenge.fastfood.fastfood.adapters.clients.PaymentClient
import tech.challenge.fastfood.fastfood.common.enums.OrderStatusEnum
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderItemGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.ProductGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order
import tech.challenge.fastfood.fastfood.entities.OrderItem

@Service
class CreateOrderUseCase(
    private val orderGatewayInterface: OrderGatewayInterface,
    private val orderItemGatewayInterface: OrderItemGatewayInterface,
    private val productGatewayInterface: ProductGatewayInterface,
    // private val createPaymentUseCase: CreatePaymentUseCase,
    private val paymentClient: PaymentClient,
) {

    fun execute(order: Order): Order {
        validateOrderItems(orderItems = order.orderItems)

        val orderEntity = orderGatewayInterface.save(order.copy(status = OrderStatusEnum.PENDING_AUTHORIZATION))
        val orderItems: List<OrderItem> = order.orderItems.map { it.copy(orderId = orderEntity.id) }
        val savedOrderItems = orderItemGatewayInterface.saveAll(orderItems)
        val orderItemsWithProductInfo = savedOrderItems.map { orderItem ->
            val productInfo = productGatewayInterface.findByOrderItemId(orderItem.id!!)
            orderItem.copy(product = productInfo)
        }

        val orderWithItems = orderEntity.copy(orderItems = orderItemsWithProductInfo)
          val paymentAssociation = paymentClient.createPaymentByOrderId(orderWithItems)
        return orderWithItems.copy(
            payment = paymentAssociation
        )
    }

    private fun validateOrderItems(orderItems: List<OrderItem>) {
        if (orderItems.isEmpty()) {
            throw BadRequestException("O pedido deve conter produtos")
        }

        if (orderItems.size != orderItems.distinctBy { it.product?.id }.size) {
            val duplicatedItems = orderItems.groupingBy { it.product?.id }
                .eachCount()
                .filter { it.value > 1 }
                .keys
                .toList()

            throw BadRequestException("Produto duplicado, considere incrementar a quantidade ao invés de enviá-lo duas vezes - Itens duplicados: ${duplicatedItems}")
        }
    }
}