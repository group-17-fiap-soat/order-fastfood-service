package tech.challenge.fastfood.fastfood.adapters.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import tech.challenge.fastfood.fastfood.entities.Order
import tech.challenge.fastfood.fastfood.entities.PaymentAssociation

@FeignClient(name = "paymentClient", url = "localhost:8083")
interface PaymentClient {
    @PostMapping("/api/payments")
    fun createPaymentByOrderId(@RequestBody order: Order): PaymentAssociation?
}