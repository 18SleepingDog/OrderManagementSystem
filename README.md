# OrderManagementSystem

# Get Started 
1. create an account in https://distancematrix.ai/ to get the access token

2. Replace the environment of the app in the docker-compose.yml ACCESS_TOKEN_ENV_VAR: <Distance_AI_ACCESS_KEY> with the receieved access token from Distance Matrix AI

3. start the mysql and the spring boot application with start.sh

# Required package/ installation  

1. java 18.0.1.1

2. maven 

# Implementation Remarks

1. deliver_order_status_operation_lock Table is for the locking purpose for concurrent calling to updateh the same order uuid. Duplicated Key Error in inserting the table will jump if there is a thread is doing operation with the order uuid.

2. Cron job is built in case the application is dead and cannot release the lock in the deliver_order_status_operation_lock table in every 5 mins.

3. test container is used to test the mysql query statement operation in the application (test contianer will build a temporatory docker container to host the temporary MYSQL image for the application to connect) [Integration test]

4. mockMvc with Mockito(testing the controller layer) and Junit5 with Mockito (service layer) has been used. [unit test]  

# Notes for Further enhancement 

[Performance enhancement]

1. Caching the HTTPS request to the application for skipping the operation?

2. RPC instead of HTTPS request to the applicaiton?

3. Master and slave database to separate the read and write operations for the mysql?

[Availability]

1. K8S for replicating the pods of the application in case the the application goes down the service can still availble 

[User interface]

1. NextJS server side render user interface and can use rpc for server to server calling?
