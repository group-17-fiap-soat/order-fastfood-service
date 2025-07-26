package tech.challenge.fastfood.fastfood.adapters.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import tech.challenge.fastfood.fastfood.entities.Order
import tech.challenge.fastfood.fastfood.entities.PaymentAssociation
import java.util.*

@FeignClient(name = "paymentClient", url = "\${payment-api.url}")
@Component
interface PaymentApiClient {
    @PostMapping("/payments")
    fun createPaymentAssociation(@RequestBody order: Order): PaymentAssociation?

    @GetMapping("/payments")
    fun getPaymentByOrderId(@RequestParam orderId: UUID): List<PaymentAssociation>
}