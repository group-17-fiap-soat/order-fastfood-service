package tech.challenge.fastfood.fastfood.usecases.product

import org.springframework.stereotype.Service
import tech.challenge.fastfood.fastfood.common.dtos.ProductDto
import tech.challenge.fastfood.fastfood.common.interfaces.gateway.ProductGatewayInterface
import tech.challenge.fastfood.fastfood.adapters.presenters.ProductMapper

@Service
class FindAllProductUseCase(
    private val productGatewayInterface: ProductGatewayInterface
) {

    fun execute(category: String?): List<ProductDto> {
        val products =
            category?.let { productGatewayInterface.findAllByCategory(category) }
                ?: productGatewayInterface.findAll()
        return products.map(ProductMapper::toDto)
    }
}