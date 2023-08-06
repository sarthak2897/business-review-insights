package config

import com.typesafe.config.{Config, ConfigFactory}

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}

@Singleton
class AppConfig {
  val config = ConfigFactory.load()
  val bootstrapServers = config.getString("kafka.bootstrap.servers")
  val kafkaConsumerGroup = config.getString("kafka.consumer-group") + "-" + LocalDateTime.now()
  val kafkaTopic = config.getString("kafka.topic")
  val schemaRegistryUrl = config.getString("kafka.schema.registry")
}
