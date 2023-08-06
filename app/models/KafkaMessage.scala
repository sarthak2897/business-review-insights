package models

import akka.kafka.ConsumerMessage.CommittableOffset

case class KafkaMessage(businessReviewDetails: BusinessReview,
                        kafkaOffset : CommittableOffset)