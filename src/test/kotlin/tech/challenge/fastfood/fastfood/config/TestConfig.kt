package tech.challenge.fastfood.fastfood.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import tech.challenge.fastfood.fastfood.adapters.clients.PaymentApiClient
import tech.challenge.fastfood.fastfood.common.enums.PaymentStatusEnum
import tech.challenge.fastfood.fastfood.entities.Order
import tech.challenge.fastfood.fastfood.entities.PaymentAssociation
import tech.challenge.fastfood.fastfood.entities.PaymentData
import java.math.BigDecimal
import java.util.*

@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun mockPaymentApiClient(): PaymentApiClient {
        return object : PaymentApiClient {
            override fun getPaymentByOrderId(orderId: UUID): List<PaymentAssociation> {
                val paymentData = PaymentAssociation(
                    paymentData = PaymentData(
                        id = UUID.randomUUID(),
                        orderId = orderId,
                        totalAmount = BigDecimal.TEN,
                        paymentMethod = "PIX",
                        paymentStatus = PaymentStatusEnum.PAYMENT_PENDING
                    )
                )
                return listOf(paymentData)
            }

            override fun createPaymentAssociation(order: Order): PaymentAssociation? {
                return PaymentAssociation(
                    paymentData = PaymentData(
                        id = UUID.randomUUID(),
                        orderId = order.id!!,
                        totalAmount = BigDecimal.TEN,
                        paymentMethod = "PIX",
                        paymentStatus = PaymentStatusEnum.PAYMENT_PENDING
                    ),
                    data = mapOf("mock" to true)
                )
            }
        }
    }
}
