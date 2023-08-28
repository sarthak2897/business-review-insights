# business-review-insights
Application built in scala, akka and play framework to produce review messages from json file and consume those messages to transform and serve it to Cassandra.

# How to test schema registry integration as a data contract with kafka
1. Add a new data model which would be produced as an avro message
2. Create a new topic and add the correct avro message as part of the schema registry
3. Add the logic to produce the kafka message as new data model
4. Consume the message and there would be an error decoding message which would mean violation of data contract