# Introduction

This demo shows enabling authentication to `minikube` with a locally deployed `Keycloak` server. In order to simplify
setup around connecting `minikube` to Keycloak [ngrok](https://ngrok.com/) is leveraged to provide a public DNS with a 
valid certificate.


## Setup `ngrok`

Register with [ngrok](https://ngrok.com/) and follow the steps to [get started](https://dashboard.ngrok.com/get-started/setup).

Find the free domain provided in the [ngrok dashboard](https://dashboard.ngrok.com/cloud-edge/domains)

Edit `conf/env` and add the following line:
```
KEYCLOAK_DOMAIN=<ngrok domain name>
```

Start `ngrok` with:
```
./ngrok-start.sh
```


# Setup Keycloak

Edit `conf/env` and add the following lines:
```
KEYCLOAK_HOME=<path to Keycloak installation>
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=<some random password>
```

Start Keycloak with:
```
./keycloak-start.sh
```

Edit `conf/env` and add the following lines:
```
REALM_NAME=myrealm
CLIENT_ID=myclient
CLIENT_SECRET=<some random secret>
USER_NAME=myuser
USER_PASSWORD=<some random password>
GROUP_NAME=mygroup
```

After it has started create the realm, clients, etc. needed to secure `minikube` with:
```
./realm-create.sh
```

# Setup minikube

Start minikube with:
```
./minikube-start.sh
```

Create some namespaces we'll use to test access with:
```
./namespaces-create.sh
```

# Testing things

## Passing `--token`

Create a separate config entry for `kubectl` to test accessing `minikube` with an ID token:

```
./kubectl-context-token.sh
```

This creates a few different namespaces, with the following RBAC:

* `test-no-access`: User shouldn't have access to do anything
* `test-user`: User with specific email ($USER_EMAIL) can view pods
* `test-group`: User with specific group ($GROUP_NAME) can view pods

Now, try to list pods in the `test-no-access` namespace, which should fail:
```
kubectl get pods --token=$(./token.sh id) -n test-no-access
```

Now, try to list pods in the `test-user` namespace, which should fail:
```
kubectl get pods --token=$(./token.sh id) -n test-user
```

Now, try to list pods in the `test-group` namespace, which should fail:
```
kubectl get pods --token=$(./token.sh id) -n test-group
```

> If you remove the group from the user through the Keycloak admin console the above should fail
