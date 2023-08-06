package kafka

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import com.typesafe.config.Config
import config.AppConfig
import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroDeserializer, KafkaAvroDeserializerConfig}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import javax.inject.{Inject, Singleton}
import scala.jdk.CollectionConverters.MapHasAsJava

@Singleton
class KafkaConsumer @Inject() (implicit ac : ActorSystem,appConfig: AppConfig){

  val kafkaConfig: Config = ac.settings.config.getConfig("akka.kafka.consumer")

  val consumerSettings: ConsumerSettings[String, AnyRef] = ConsumerSettings(kafkaConfig, new StringDeserializer, createKafkaAvroSerializer())
    .withBootstrapServers(appConfig.bootstrapServers)
    .withGroupId(appConfig.kafkaConsumerGroup)
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest")

  val kafkaSource = Consumer.committableSource(consumerSettings,Subscriptions.topics(appConfig.kafkaTopic))


  def createKafkaAvroSerializer() = {
    val kafkaAvroSerDeConfig = Map[String, Any](
      AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> appConfig.schemaRegistryUrl)
    val kafkaAvroDeserializer = new KafkaAvroDeserializer()
    kafkaAvroDeserializer.configure(kafkaAvroSerDeConfig.asJava,false)
    kafkaAvroDeserializer
  }

}
