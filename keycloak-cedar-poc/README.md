# PoC client policy using Cedar policies

To deploy to Keycloak run `mvn install` and copy `keycloak-cedar-event-listener-1.0.0-SNAPSHOT.jar` to `KC_HOME/providers`

## Configure for a realm

Open Keycloak Admin UI and select the realm you want to enable Cedar policies for.

### Create client policy profile

Go to `Realm settings` > `Client policies` > `Profiles`, and click `Create client profile`.

Use any `Client profile name` you want (for example `cedar`), and click `Save`.

Click `Add executor` > Select `Executor type` `cedar-policies`, and click `Add`.

### Create client policy

Go to `Realm settings` > `Client policies` > `Policies`, and click `Create client policy`.

Use any `Name` you want (for example `cedar`) and click `Save`.

Click `Add condition`, Select `Condition type` `any-client`, and click `Add`.

Click `Add client profile`, Select the client policy profile you created previously, and click `Add`.

## Create a Cedar policy

Create the file `KC_HOME/data/client-policies.cedar` with whatever Cedar policy you want.

`context` contains the request parameters for the token exchange request, including:

* `audience` - `audience` paremeter as a list
* `scope` - `scope` parameter as a list
* `requestedTokenType` - `requested_token_type` parameter
* `subjectToken` - boolean indicating if `subject_token` parameter was included
* `subjectTokenType` - `subject_token_type` parameter if sent
* `actorToken` - boolean indicating if `actor_token` parameter was included
* `actorTokenType` - `actor_token_type` parameter if sent

Here's an example policy to try out: 

```
permit(
    principal,
    action,
    resource
);

forbid(
    principal == Client::"myclient2",
    action,
    resource
);

forbid(
    principal,
    action,
    resource
) when {
    context.audience.contains("aud2")
};

forbid(
    principal,
    action,
    resource
) when {
    context.scope.contains("email")
};
```

The above policy will forbid token exchange requests if one of the following conditions are true:

* The client is `myclient2`
* The `audience` contains `aud2`
* The `scope` contains `email`
