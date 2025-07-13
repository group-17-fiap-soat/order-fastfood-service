package tech.challenge.fastfood.fastfood.usecases.order

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import tech.challenge.fastfood.fastfood.adapters.clients.PaymentClient
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderItemGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order
import java.util.*

@Service
class GetOrderByIdUseCase(
    private val orderGatewayInterface: OrderGatewayInterface,
    private val orderItemGatewayInterface: OrderItemGatewayInterface,
    private val paymentClient: PaymentClient
) {

    fun execute(id: UUID): Order {
        val order = orderGatewayInterface.findById(id)
            ?: throw EntityNotFoundException("Pedido com o id ${id} não encontrado")

        val orderItems = orderItemGatewayInterface.findAllByOrderId(order.id!!)
        val paymentAssociation = paymentClient.createPaymentByOrderId(order.id.toString())

        return order.copy(
            orderItems = orderItems, payment = paymentAssociation
        )
    }
}