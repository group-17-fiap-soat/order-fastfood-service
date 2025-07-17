package tech.challenge.fastfood.fastfood.usecases.order

import org.springframework.stereotype.Service
import tech.challenge.fastfood.fastfood.adapters.clients.PaymentApiClient
import tech.challenge.fastfood.fastfood.common.enums.OrderStatusEnum
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderItemGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order

@Service
class ListOrderUseCase(
    private val orderGatewayInterface: OrderGatewayInterface,
    private val orderItemGatewayInterface: OrderItemGatewayInterface,
    private val paymentApiClient: PaymentApiClient
) {
    fun execute(): List<Order> {
        val orders = orderGatewayInterface.findAll()
        return orders.map { order ->
            val orderItems = orderItemGatewayInterface.findAllByOrderId(order.id!!)
            val paymentAssociation = paymentApiClient.createPaymentByOrderId(order.id.toString())
            order.copy(
                orderItems = orderItems, payment = paymentAssociation
            )
        }
            .filter { it.status != OrderStatusEnum.FINISHED }
            .sortedWith(compareBy<Order> { it.status?.priority }
                .thenBy { it.createdAt })
    }
}
