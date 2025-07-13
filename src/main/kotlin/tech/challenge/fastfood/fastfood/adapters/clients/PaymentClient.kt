package tech.challenge.fastfood.fastfood.adapters.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import tech.challenge.fastfood.fastfood.entities.PaymentAssociation

@FeignClient(name = "paymentClient", url = "localhost:8083")
interface PaymentClient {
    @PostMapping("/payments/order/{orderId}")
    fun createPaymentByOrderId(@PathVariable orderId: String): PaymentAssociation?
}