* Try out using `kc-oidc` as a plugin to `oc`
* OpenShift console hard-codes the client `console`, which will prevent using securing multiple OpenShift clusters with the same realm
* Configuration is using unsupported APIs to configure OpenShift, and is a bit brittle and hard to debug
* `console` client is using wildcard for redirect-uri which is far from ideal