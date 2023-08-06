package modules

import akka.actor.ActorSystem
import akka.kafka.CommitterSettings
import akka.kafka.scaladsl.Committer
import akka.stream.{Materializer, OverflowStrategy}
import com.google.inject.Singleton
import kafka.KafkaConsumer
import modules.AppFlows.processKafkaMessage
import play.api.Logger

import javax.inject.Inject
import scala.concurrent.ExecutionContext

@Singleton
class BusinessReviewDetailsConsumerApplication @Inject() (implicit val ac : ActorSystem,
                                                          implicit val ec : ExecutionContext,
                                                          implicit val mat : Materializer,
                                                          kafkaConsumer: KafkaConsumer) {

  val parallelism = 10
  final val logger : Logger = Logger.apply(this.getClass)

  logger.info("Consuming business review data to move to Cassandra")

  kafkaConsumer.kafkaSource
    .buffer(500, OverflowStrategy.backpressure)
    .mapAsync(parallelism)(processKafkaMessage)
    .map(x => {
      logger.info(""+x)
      x.kafkaOffset
    })
    .runWith(Committer.sink(CommitterSettings(ac)))
}
