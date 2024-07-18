# Extension to help preview custom themes

Build and copy to Keycloak:

```
mvn package
cp target/theme-preview.jar $KEYCLOAK_HOME/providers/
```

Start Keycloak and open `http://localhost:8080/realms/master/theme-preview/`

This will list pages that currently can be previewed. Click on the page you want to preview.

The configuration for the page can be altered by supplying query parameters, which will alter
the realm configuration through a proxy.

At the moment it is only possible to alter realm configuration that returns Boolean or String 
parameters, and take no arguments.

For example to enable user registration add `?realm.registrationAllowed=true`, or to change the realm
display name set `?realm.displayNameHtml=My realm`.

To view everything that can be altered take a look at [RealmModel.java](https://github.com/keycloak/keycloak/blob/main/server-spi/src/main/java/org/keycloak/models/RealmModel.java).
Anything that returns a boolean or a String and takes no arguments can be override. The query parameter name is
without `is` and `get` and the first-letter is lower-cased. For example `boolean isRegistrationAllowed()` can be 
overriden with `?registrationAllowed=true`.
