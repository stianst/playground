

kubectl config set-credentials --exec-api-version=client.authentication.k8s.io/v1 --exec-command='kc-oidc token --type id' kccli

kubectl config set-context kccli --cluster=minikube --user kccli
kubectl config use-context kccli



```
- name: kccli
  user:
    exec:
      apiVersion: client.authentication.k8s.io/v1
      args:
      - kc
      - kubectl
      command: kubectl
      env: null
      interactiveMode: IfAvailable
      provideClusterInfo: false
```

```
- context:
    cluster: minikube
    user: kccli
  name: kccli
```