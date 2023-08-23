package kafka

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import com.sksamuel.avro4s.{Record, RecordFormat}
import config.AppConfig
import io.confluent.kafka.serializers.{AbstractKafkaAvroSerDeConfig, KafkaAvroSerializer}
import models.{BusinessReview, BusinessReviewExtra}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import javax.inject.{Inject, Singleton}
import scala.jdk.CollectionConverters.MapHasAsJava

/* Ingest data from a csv file using akka streams
and send the message to topic after validating from schema registry*/

@Singleton
class KafkaProducer @Inject() (implicit ac : ActorSystem, appConfig: AppConfig) {

  val kafkaAvroConfig: Map[String, Any] = Map[String,Any](
    AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> appConfig.schemaRegistryUrl)

  val producerSettings: ProducerSettings[String, AnyRef] = {

    val kafkaAvroSerializer = new KafkaAvroSerializer()
    kafkaAvroSerializer.configure(kafkaAvroConfig.asJava,false)
    //val serializer = kafkaAvroSerializer.asInstanceOf[Serializer[BusinessReview]]

    ProducerSettings(ac, new StringSerializer, kafkaAvroSerializer)
      .withBootstrapServers(appConfig.bootstrapServers)
  }

  def produceBusinessReviewMessages(businessReview: BusinessReview) = {
    //To test incorrect produced message different from one in schema registry
    val bre = BusinessReviewExtra(businessReview.reviewId
      ,businessReview.businessId,businessReview.userId,businessReview.stars)
    val value: Record = RecordFormat[BusinessReviewExtra].to(bre)
    new ProducerRecord[String,AnyRef](appConfig.kafkaTopic, null, value)
  }
}
