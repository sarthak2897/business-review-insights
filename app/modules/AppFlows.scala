package modules

import akka.kafka.ConsumerMessage
import akka.stream.Supervision
import com.sksamuel.avro4s.RecordFormat
import exception.InvalidJsonMessageException
import models.{BusinessReview, KafkaMessage}
import org.apache.avro.generic.GenericRecord
import play.api.Logger
import play.api.libs.json.{JsError, JsResult, JsSuccess, Json}

import scala.concurrent.{ExecutionContext, Future}

object AppFlows {

  final val logger : Logger = Logger(this.getClass)

  def transformBusinessReviewMessages(brMessage : String) (implicit ec : ExecutionContext) = {
      val msg = Json.parse(brMessage)
      val businessReview: JsResult[BusinessReview] = Json.fromJson[BusinessReview](msg)
      businessReview match {
        case JsSuccess(br: BusinessReview, path) => Future(br)
        case e@JsError(_) => throw new InvalidJsonMessageException("Error while processing kafka message : " + msg + " : " + JsError.toJson(e).toString())
      }
  }

  def processKafkaMessage(msg: ConsumerMessage.CommittableMessage[String, AnyRef])
                         (implicit ec: ExecutionContext) = {
    val kafkaMsg: BusinessReview = RecordFormat[BusinessReview].from(msg.record.value().asInstanceOf[GenericRecord])
    Future(KafkaMessage(kafkaMsg, msg.committableOffset))
  }

  val decider : Supervision.Decider = {
    case e : Exception =>
      logger.error("Error occurred : "+ e.printStackTrace())
      Supervision.Stop
  }
}
