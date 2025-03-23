package tech.challenge.fastfood.fastfood.common.dto.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import tech.challenge.fastfood.fastfood.common.enums.CategoryEnum
import java.math.BigDecimal
import java.util.*

data class UpdateProductRequestV1 (
    val id: UUID? = null,

    @field:NotNull(message = "Nome não pode estar vazio.")
    val name: String,

    val description: String?,

    @field:NotNull(message = "Campo não pode estar vazio.")
    val price: BigDecimal,

    @field:NotNull(message = "Campo não pode estar vazio.")
    val category: CategoryEnum,

    val imageUrl: String?
)
