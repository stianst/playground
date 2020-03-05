
Start Keycloak:

    docker run -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 8180:8080 quay.io/keycloak/keycloak


Install and start WildFly 8.2

Deploy example:

    mvn clean wildfly:deploy
    
    
Open the following URLs and check if you get an access token in the response:

* http://localhost:8080/resteasy-client-issue/servlet
* http://localhost:8080/resteasy-client-issue/rest

Both should work fine and you should see `Token <BASE 64 TOKEN>`.

Stop WildFly 8.


Install and start WildFly 18. Then repeat the steps above. Observe that `/rest` works, while `/servlet` does not.



