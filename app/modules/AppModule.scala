package modules

import com.google.inject.AbstractModule
import config.AppConfig
import dao.CassandraDao

class AppModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AppConfig]).asEagerSingleton()
  //  bind(classOf[CassandraDao]).asEagerSingleton()
    //bind(classOf[BusinessReviewDetailsProducerApplication]).asEagerSingleton()
    bind(classOf[BusinessReviewDetailsConsumerApplication]).asEagerSingleton()
  }
}
