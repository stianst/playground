# Setup Keycloak

Securing `minikube` with Keycloak requires Keycloak running somewhere with a valid TLS certificate. The simplest way to
achieve this is to run it locally and expose it to the internet with a service like [ngrok](https://ngrok.com/).

## Setup `ngrok` ingress

Register with [ngrok](https://ngrok.com/) and follow the steps to [get started](https://dashboard.ngrok.com/get-started/setup).

Find the free domain provided in the [ngrok dashboard](https://dashboard.ngrok.com/cloud-edge/domains)

Edit the file `conf/env` and add the following line:
```
KEYCLOAK_DOMAIN=living-bluebird-credible.ngrok-free.app
```

Start your `ingress` pointing to port `8080` with:
```
source conf/env
ngrok http --domain=$NGROK_DOMAIN 8080
```


## Start Keycloak locally

Download Keycloak and run:

```
bin/kc.sh start-dev --hostname-url=https://<DOMAIN> --hostname-admin-url=https://<DOMAIN>
```

> You need to specify both hostname-url and hostname-admin-url until https://github.com/keycloak/keycloak/issues/23411 is resolved

Now you should be able to access Keycloak on `https://<DOMAIN>`.

## Create a realm

Open https://<DOMAIN> in your favourite web browser, and login to the Keycloak admin console.

```
export REALM_NAME=minikube2

./kcadm.sh create realms -s realm=$REALM_NAME

./kcadm.sh create groups -r $REALM_NAME -s name=mygroup

./kcadm.sh create users -r $REALM_NAME -s username=myuser
./kcadm.sh set-password -r $REALM_NAME --username myuser --new-password mypassword

USER_ID=$(./kcadm.sh get users -r $REALM_NAME -q username=myuser --fields id --format csv --noquotes)
GROUP_ID=$(./kcadm.sh get -r $REALM_NAME groups -q q=mygroup -q exact=true --fields id --format csv --noquotes)

./kcadm.sh update -r $REALM_NAME users/$USER_ID/groups/$GROUP_ID

./kcadm.sh create clients -r $REALM_NAME \
    -s clientId=myclient \
    -s enabled=true \
    -s clientAuthenticatorType=client-secret \
    -s secret=mysecret \
    -s 'redirectUris=["*"]'
    
CLIENT_ID=$(./kcadm.sh get clients -r $REALM_NAME -q clientId=myclient --fields id --format csv --noquotes)

./kcadm.sh create -r $REALM_NAME clients/$CLIENT_ID/protocol-mappers/models -b '{"protocol":"openid-connect","protocolMapper":"oidc-group-membership-mapper","name":"groups","config":{"claim.name":"groups","full.path":"false","id.token.claim":"true","access.token.claim":"true","userinfo.token.claim":"true"}}'

```

# Setup Minikube

Follow the steps from the [minikube docs](https://minikube.sigs.k8s.io/docs/start/) to install `minikube`, but run the 
start command with the following options to configure authentication with Keycloak:

```
minikube start \
    --extra-config=apiserver.authorization-mode=RBAC \
    --extra-config=apiserver.oidc-issuer-url=https://living-bluebird-credible.ngrok-free.app/realms/kube \
    --extra-config=apiserver.oidc-username-claim=email \
    --extra-config=apiserver.oidc-groups-claim=groups \
    --extra-config=apiserver.oidc-client-id=kubectl
```

