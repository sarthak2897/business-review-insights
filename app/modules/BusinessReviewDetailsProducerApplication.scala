package modules

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.stream.ActorAttributes.SupervisionStrategy
import akka.stream.{ActorAttributes, Materializer, Supervision}
import akka.stream.scaladsl.{FileIO, Framing}
import akka.util.ByteString
import kafka.KafkaProducer
import play.api.Logger
import modules.AppFlows._

import java.nio.file.Paths
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import com.google.inject.Singleton

@Singleton
class BusinessReviewDetailsProducerApplication @Inject()(implicit val ac : ActorSystem,
                                                         implicit val ec : ExecutionContext,
                                                         implicit val mat : Materializer,
                                                         val kafkaProducer: KafkaProducer){

  final val logger : Logger = Logger(this.getClass)
  logger.info("Producing business review data")
  val parallelism = 10
  //Test file : conf\test_file.json
  FileIO.fromPath(Paths.get("C:\\gcs_capstone_dezoomcamp\\yelp_academic_dataset_review.json"))
    .via(Framing.delimiter(ByteString("\n"),8092))
    .map(_.utf8String).take(15)
    .mapAsync(parallelism)(transformBusinessReviewMessages)
    .map(kafkaProducer.produceBusinessReviewMessages)
    .withAttributes(ActorAttributes.supervisionStrategy(decider))
    .runWith(Producer.plainSink(kafkaProducer.producerSettings))
    .recover(e => logger.error("Error occurred : "+e.printStackTrace()))



}
