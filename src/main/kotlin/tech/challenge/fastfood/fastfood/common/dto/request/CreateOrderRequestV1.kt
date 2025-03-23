package tech.challenge.fastfood.fastfood.common.dto.request

import jakarta.validation.constraints.NotNull
import java.util.*

data class CreateOrderRequestV1(
    val customerId: UUID? = null,

    @field:NotNull(message = "Pedido não pode estar vazio.")
    val orderItems: List<CreateOrderItemRequestV1> = listOf()
)
