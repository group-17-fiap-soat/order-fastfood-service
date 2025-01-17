package tech.challenge.fastfood.fastfood.infra.mapper

import tech.challenge.fastfood.fastfood.application.dto.OrderItemDto
import tech.challenge.fastfood.fastfood.domain.model.OrderItemEntity
import tech.challenge.fastfood.fastfood.infra.adapters.controllers.request.CreateOrderItemRequestV1

object OrderItemMapper {

    fun createOrderItemRequestToDto(requestV1: CreateOrderItemRequestV1) = OrderItemDto(
        productId = requestV1.productId,
        quantity = requestV1.quantity
    )

    fun toEntity(orderItemDto: OrderItemDto) = OrderItemEntity(
        id = orderItemDto.id,
        productId = orderItemDto.productId,
        orderId = orderItemDto.orderId,
        quantity = orderItemDto.quantity,
        createdAt = orderItemDto.createdAt,
        updatedAt = orderItemDto.updatedAt
    )

    fun toDto(orderItemEntity: OrderItemEntity) = OrderItemDto(
        id = orderItemEntity.id,
        productId = orderItemEntity.productId,
        orderId = orderItemEntity.orderId,
        quantity = orderItemEntity.quantity,
        createdAt = orderItemEntity.createdAt,
        updatedAt = orderItemEntity.updatedAt
    )



}