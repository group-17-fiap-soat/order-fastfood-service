package tech.challenge.fastfood.fastfood.usecases.product

import org.springframework.stereotype.Service
import tech.challenge.fastfood.fastfood.common.dtos.ProductDto
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.ProductGatewayInterface
import tech.challenge.fastfood.fastfood.adapters.presenters.ProductMapper

@Service
class CreateProductUseCase(
    private val productGatewayInterface: ProductGatewayInterface
) {

    fun execute(productDto: ProductDto): ProductDto {
        return ProductMapper.toDto(productGatewayInterface.save(productDto))
    }
}