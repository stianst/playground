Is there any check on aud, or anything else in the token? Seems to accept any valid token.

API Server accepts ID Tokens. ID Tokens are aimed for authentication to an RP, not to authorize requests to a Resource 
Server. Should use Access Tokens. However, these are opaque to the Resource Server, in which case it is required to either
have Access Tokens in a JWT format the Resource Server can understand, or introspect using the Token Introspection endpoint.
OpenShift/Kube doesn't seem to allow any sort of configuration around the token claims though, so may be better to just
have a custom TokenReview endpoint like what we had in the past.

For `oc login` integration it is possible to either have a custom flow that enables the built-in proprietary protocol
with HTTP challenge, but this is not really suitable in modern deployments as it would only support username/password
authentication, and is also no longer recommended. Should be using Authorization Code flow or device flow for better
integration with IdPs. Should also rely on Refresh Token, and send Access Tokens to the API server rather than long
expiration ID Tokens. In general though `oc` does not need an OpenID Connect flow, but just an OAuth flow.

Not quite sure what the OpenShift console does in terms of authentication. 

These flows are standard stuff though, it shouldn't be expected that a provider needs to write plugins to `oc` or the
`OpenShift console`.
