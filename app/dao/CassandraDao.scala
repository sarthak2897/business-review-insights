package dao

import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.{CassandraSessionSettings, CassandraWriteSettings}
import akka.stream.alpakka.cassandra.scaladsl.{CassandraFlow, CassandraSessionRegistry}
import akka.{Done, NotUsed}
import akka.stream.scaladsl.Flow
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.{BoundStatement, PreparedStatement, SimpleStatement}
import models.{BusinessReview, KafkaMessage}
import play.api.Logger

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CassandraDao @Inject() (val ac : ActorSystem) {

  final val logger : Logger = Logger.apply(this.getClass)

  //private val session = CqlSession.builder().build()

  val sessionSettings = CassandraSessionSettings()
  implicit val cassandraSession = CassandraSessionRegistry.get(ac).sessionFor(sessionSettings)

  private val insertQuery = "INSERT INTO cass_keyspace.business_reviews (review_id,user_id,business_id" +
    ",stars,review_message) VALUES (?,?,?,?,?)"

  val statementBinder : (KafkaMessage, PreparedStatement) => BoundStatement =
    (kafkaMsg,preparedStatement) => {
      val br = kafkaMsg.businessReviewDetails
      preparedStatement.bind(br.reviewId, br.userId, br.businessId, br.stars, br.reviewMessage)
    }


  val cassandraFlow: Flow[KafkaMessage, KafkaMessage, NotUsed] = {
    CassandraFlow.create(CassandraWriteSettings.defaults,insertQuery,statementBinder)
  }


//  val createTable = {
//    logger.info("Using keyspace and creating cassandra table business_reviews")
//    //session.execute("""USE cass_keyspace;""")
//    session.execute("""
//      |CREATE TABLE IF NOT EXISTS cass_keyspace.business_reviews(
//      |review_id TEXT PRIMARY KEY,
//      |user_id TEXT,
//      |business_id TEXT,
//      |stars DOUBLE,
//      |review_message TEXT
//      |);
//      |""".stripMargin)
//  }

  //val prepareQuery = session.prepare(SimpleStatement.newInstance(insertQuery))

//  def insertBusinessReviewToCassandra(br : BusinessReview)(implicit ec : ExecutionContext) = {
//    Future(session.execute(prepareQuery
//       .bind(br.reviewId, br.userId, br.businessId, br.stars, br.reviewMessage)))
//  }

}
