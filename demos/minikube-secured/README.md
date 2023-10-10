# Introduction

This demo shows enabling authentication to `minikube` with a locally deployed `Keycloak` server. In order to simplify
setup around connecting `minikube` to Keycloak [ngrok](https://ngrok.com/) is leveraged to provide a public DNS with a 
valid certificate.


## Prerequisites

* [Keycloak](https://www.keycloak.org/getting-started/getting-started-zip)
* [minikube](https://minikube.sigs.k8s.io/docs/start/)
* [kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl)
* [ngrok](https://dashboard.ngrok.com/get-started/setup)
* [Keycloak OIDC CLI](https://github.com/stianst/keycloak-oidc-cli)


## Create environment file

The demo uses `conf/env` as a configuration file, this file can be created by running:

```
./create-env.sh <Keycloak home> <ngrok domain>
```

Where `<Keycloak home>` should be the full path of where you have Keycloak installed, and `<ngrok domain>` should point 
to your free ngrok domain (available from the [ngrok dashboard](https://dashboard.ngrok.com/cloud-edge/domains)).


## Start `ngrok`

Start ngrok 
```
./ngrok-start.sh
```

# Start Keycloak

First, start the ngrok tunnel:
```
./ngrok-start.sh
```

In a separate terminal, start Keycloak:
```
./keycloak-start.sh
```

After it has started create the realm, clients, etc. needed to secure `minikube` with:
```
./keycloak-realm-create.sh
```

# Start minikube

Start minikube with:
```
./minikube-start.sh
```

Create some namespaces we'll use to test access with:
```
./minikube-namespaces-create.sh
```

# Add configuration context for kc-oidc

Create two configuration context for kc-oidc with:
```
./kc-oidc-configure.sh
```

Then get a token for the user with:
```
kc-oidc token --context=minikube-demo-user --decode
```

Compare with the token for the kube admin with:
```
kc-oidc token --context=minikube-demo-admin --decode
```

# Testing things

## Using `--token` option

`kubectl` seems to ignore the `--token` option with the default configuration context created for `minikube`. To 
prevent this create a separate context with:

```
./kubectl-context-token.sh
```

You can switch back to the default context with:
```
kubectl config use-context minikube
```

First try to list pods in namespace with no access:
```
kubectl get pods --token=$(kc-oidc token --context=minikube-demo-user) --namespace test-no-access
```

Then list pods with access based on user:
```
kubectl get pods --token=$(kc-oidc token --context=minikube-demo-user) --namespace test-user
```

Then list pods with access based on group:
```
kubectl get pods --token=$(kc-oidc token --context=minikube-demo-user) --namespace test-group
```

Finally, you can try the kube admin user:
```
kubectl get namespaces --token=$(kc-oidc token --context=minikube-demo-admin)
```

# Using `kc-oidc` as a `kubectl` plugin

See [documentation for kc-oidc](https://github.com/stianst/keycloak-oidc-cli#kubernetes-command-line-tool-kubectl) on
how to configure `kc-oidc` as a authentication plugin for `kubectl`. After you've done this you can try the examples in
the previous section, but without the need to use `--token=$(kc-oidc ...)` to pass the tokens. For example 

```
kubectl get pods --namespace test-user
```