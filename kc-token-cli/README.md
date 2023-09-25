# keycloak-token-cli

Build with:
```
mvn clean install -Dnative
```

Then create a context, for example:
```
target/kc-oidc-runner config set --context=mycontext --issuer=http://localhost:8080/realms/master --flow=resource-owner --client-id=admin-cli --user=admin --user-password=admin
```

Then, get an access token with:
```
target/kc-oidc-runner token
```

Or, an ID token with:
```
target/kc-oidc-runner token --type=id
```

You can also display the token decoded with:
```
target/kc-oidc-runner token --decode
```

You can create multiple contexts if you'd like, and select the default:
```
target/kc-oidc-runner config use --context=mycontext
```

Or, use a specific context with the token command:
```
target/kc-oidc-runner token --context=myothercontext
```

Finally, all config and tokens are stored in:
```
~/.kc/oidc.yaml
```

Well, you could also choose not to store tokens for a specific context:
```
target/kc-oidc-runner config update --context=mycontext --store-tokens=false
```

Or, update something else for an existing context:
```
target/kc-oidc-runner config update --context=mycontext --client-id=myotherclient
```

Or, use 'null' as the value to remove something:
```
target/kc-oidc-runner config update --context=mycontext --client-id=null
```

Finally, if you get confused about contexts, you can list all contexts with:
```
target/kc-oidc-runner config view
```

Or, a specific context:
```
target/kc-oidc-runner config view --context=mycontext
```

Or, if you don't know what the current default context is (that you set with context-use):
```
target/kc-oidc-runner config view --context=mycontext
```
