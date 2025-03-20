package tech.challenge.fastfood.fastfood.usecases.order

import org.springframework.stereotype.Service
import tech.challenge.fastfood.fastfood.adapters.presenters.OrderMapper
import tech.challenge.fastfood.fastfood.common.daos.OrderDAO
import tech.challenge.fastfood.fastfood.common.enums.OrderStatusEnum
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.OrderGatewayInterface
import tech.challenge.fastfood.fastfood.entities.Order

@Service
class ListOrderUseCase(
    private val orderGatewayInterface: OrderGatewayInterface
) {

    fun execute(): List<Order> {
        val orders = orderGatewayInterface.findAll()
            .filter { it.status != OrderStatusEnum.FINISHED }
            .sortedWith(compareBy<Order> { it.status?.priority }
                .thenBy { it.createdAt })
        return orders
    }
}
