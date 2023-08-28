package models

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{Reads, __}

case class BusinessReview(reviewId : String,
                          userId : String,
                          businessId : String,
                          stars : Double,
                          reviewMessage : String)

case class BusinessReviewExtra(reviewId : String,
                               userId : String,
                               businessId : String,
                               stars : Double,
                               //reviewMessage : String,
                               businessType : String = "Other")

object BusinessReview {
  implicit val reads : Reads[BusinessReview] = (
    (__ \ "review_id").read[String] and
      (__ \ "user_id").read[String] and
      (__ \ "business_id").read[String] and
      (__ \ "stars").read[Double] and
      (__ \ "text").read[String]
    ) (BusinessReview.apply _)
}

