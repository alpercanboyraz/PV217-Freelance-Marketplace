package com.alpercan.freelance.notification;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class NotificationService {

    // Kafka'dan mesaj geldiği an bu metot tetiklenir
    @Incoming("orders-in")
    public void processOrder(String message) {
        System.out.println("==================================================");
        System.out.println("🔔 NOTIFICATION SERVICE: A new event has been detected!");
        System.out.println("📩 Message Content: " + message);
        System.out.println("📧 Sending 'Your Order Has Been Confirmed' email to the recipient...");
        System.out.println("✅ Email sent (Simulation)");
        System.out.println("==================================================");

    }
}