# 🔔 Notification Service

RabbitMQ və Spring Boot istifadə edərək qurulmuş
asinxron bildiriş sistemi. Bir sorğu göndərildikdə
sistem eyni anda Email, SMS və Log bildirişlərini
paralel şəkildə çatdırır.

##  Texnologiyalar

- Java 17
- Spring Boot 3
- RabbitMQ (Fanout Exchange, Dead Letter Queue)
- Docker
- Lombok

##  Arxitektura
```
POST /api/v1/notify
        ↓
  NotificationProducer
        ↓
  Fanout Exchange
   ↙     ↓     ↘
Email   SMS    Log
Queue  Queue  Queue
  ↓      ↓      ↓
Email   SMS    Log
Consumer Consumer Consumer
```

Uğursuz mesajlar Dead Letter Queue-ya (DLQ)
yönləndirilir — sistem dayanıqlıdır.

##  Necə İşə Salmaq Olar?

Əvvəlcə Docker-də RabbitMQ-nu başlat:
```bash
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:4.0-management
```

Sonra layihəni işə sal və bu sorğunu göndər:
```bash
POST http://localhost:8080/api/v1/notify

{
  "recipient": "test@example.com",
  "subject": "Xoş gəldiniz!",
  "content": "Qeydiyyatınız tamamlandı."
}
```

##  Layihə Strukturu

src/main/java/org/example/
├── config/        # RabbitMQ konfiqurasiyası
├── controller/    # REST endpoint
├── producer/      # Mesaj göndərən
├── consumer/      # Email, SMS, Log consumer-ləri
└── model/         # NotificationMessage modeli