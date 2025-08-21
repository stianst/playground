# SPIFFE and Keycloak demo

This demo shows how to leverage SPIFFE SVIDs to authenticate clients with Keycloak.

## Starting the Spire Server and Agent

Install and start SPIRE Server:
```shell
./start-spire-server.sh
```

Start the SPIRE Agent:
```shell
./start-spire-agent.sh
```

Register the workload:
```shell
./register-workload.sh
```

Fetch a JWT SVID to verify things are working so far:
```shell
./fetch-jwt-svid.sh
```

## Staring the Spire OIDC Discovery Provider

Start the Spire OIDC Discovery provider:
```shell
./start-spire-oidc-discovery-provider.sh
```

Verify the JWKS endpoint is working with:
```shell
curl http://localhost:8082/keys
```

## Decoding and verifying the JWT SVID with `kct`

[kct](https://github.com/stianst/keycloak-tokens-cli) can be used to decode the JWT SVID as well as verify the signature. This step is optional, but will allow you to see what claims are included.

To decode  the JWT SVID run:
```shell
kct decode $(./fetch-jwt-svid.sh) --jwks=http://localhost:8082/keys
```

## Configuring Keycloak

You need to first have Keycloak with the experimental SPIFFE JWT SVID client authentication feature enabled (`./kc.sh start-dev --features=spiffe-jwt`).

As of writing this is available in https://github.com/stianst/keycloak/tree/spiffe-jwt-svids, but is expected to be available in nightly releases soon and in Keycloak 26.4.0.

Optionally create a new realm:
```bash
./kcadm.sh create realms -s realm=spiffe -s enabled=true
```

Then create a new client within the realm:
```bash
./kcadm.sh create clients -r spiffe  -f - << EOF
{
  "clientId": "spiffe://example.org/myclient",
  "serviceAccountsEnabled": true,
  "clientAuthenticatorType": "spiffe-jwt",
  "attributes": {
    "spiffeTrustDomain": "example.org",
    "use.jwks.url": true,
    "jwks.url": "http://localhost:8082/keys"
  }
}
EOF
```

## Try it out

To verify the client can now authenticate to Keycloak with the SPIFFE JWT SVID we'll do a client credential grant:
```shell
./client-credential-grant.sh
```

The output should be an access token response including an access token.

This approach to authenticating the client works regardless of what grant_type is used or what endpoints are used.

For example try doing a token introspection request with:
```shell
./token-introspection.sh
```