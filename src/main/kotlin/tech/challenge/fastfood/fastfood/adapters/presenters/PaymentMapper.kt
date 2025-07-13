package tech.challenge.fastfood.fastfood.adapters.presenters

import tech.challenge.fastfood.fastfood.common.dto.response.PaymentResponseV1
import tech.challenge.fastfood.fastfood.entities.PaymentAssociation
import tech.challenge.fastfood.fastfood.entities.PaymentData
import java.util.*

object PaymentMapper {
    fun associationToResponse(association: PaymentAssociation)=
         PaymentResponseV1(
           id = association.paymentData?.id,
           externalId = association.paymentData?.externalId,
           data = association.data
        )

}
