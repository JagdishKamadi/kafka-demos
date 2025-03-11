## Start the zookeeper and apache kafka in order to run producer and consumer app
## follow the below step

## Start with  Kafka

# To start the zookeeper
# Make sure that only kakfa folder should exist(software/kakfa/bin&etc)
Open the terminal in windows directory(C:\Software\kafka) then run the below command
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

# To start apache kafka server
bin\windows\kafka-server-start.bat config\server.properties

#Create the topic
bin\windows\kafka-topics.bat --create --topic first-topic  --bootstrap-server localhost:9092

#Write the some event into the topic
bin\windows\kafka-console-producer.bat --topic first-topic --bootstrap-server localhost:9092

#Read the event
bin\windows\kafka-console-consumer.bat --topic first-topic --from-beginning --bootstrap-server localhost:9092

# Refer the below documenation it's easy and simple
https://kafka.apache.org/quickstart