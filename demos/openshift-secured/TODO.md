* Try out using `kc-oidc` as a plugin to `oc`
* OpenShift console hard-codes the client `console`, which will prevent using securing multiple OpenShift clusters with the same realm
* Configuration is using unsupported APIs to configure OpenShift, and is a bit brittle and hard to debug
* `console` client is using wildcard for redirect-uri which is far from ideal
* Logout from OpenShift console is not forwarded
* Unsure if OpenShift console uses ID tokens / access tokens, or cookies when invoking api server
* Not sure if OpenShift console is uncluded `openid` scope
* Scoping tokens with oauth scopes (i.e. scope=mycluster:get-pods)
* Some more support for handling different claims
* Using `kc-oidc` as a plugin with `oc` or `kubectl` requires setting `exec.interactiveMode` manually. Doesn't seem to be possible through `set-credentials` or `set`
