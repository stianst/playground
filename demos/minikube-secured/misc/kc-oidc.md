

kubectl config set-credentials --username kccli --exec-api-version=client.authentication.k8s.io/v1 --exec-command='kc-oidc token --type id' kccli

kubectl config set-context kccli --cluster=minikube --user kccli
kubectl config use-context kccli