https://github.com/int128/kubelogin

```
- name: oidc
  user:
  exec:
  apiVersion: client.authentication.k8s.io/v1
  args:
  - oidc
  - get-token
  - --oidc-issuer-url=https://<DOMAIN>/realms/<REALM>
  - --oidc-client-id=<CLIENT>
  - --oidc-client-secret=<SECRET>
  command: kubectl
  env: null
  provideClusterInfo: false
  interactiveMode: IfAvailable
```

- name: oidc
  user:
  exec:
  apiVersion: client.authentication.k8s.io/v1beta1
  args:
  - oidc
  - get-token
  - --oidc-issuer-url=https://living-bluebird-credible.ngrok-free.app/realms/myrealm
  - --oidc-client-id=myclient
  - --oidc-client-secret=secret
  command: kubectl
  env: null
  interactiveMode: IfAvailable
  provideClusterInfo: false