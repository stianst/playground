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

Verify the bundle endpoint is working with:
```shell
curl --insecure https://localhost:8543
```

## Configuring Keycloak

You need to first have Keycloak with the experimental External JWT client auth and SPIFFE features enabled, and disable
trust manager for outgoing https requests to retrieve the SPIFFE bundle:
```
./kc.sh start-dev --features=client-auth-federated,spiffe --spi-connections-http-client--default--disable-trust-manager=true
```

As of writing this is available in https://github.com/stianst/keycloak/tree/spiffe-jwt-svids-with-idp, but is expected to be available in nightly releases soon and in Keycloak 26.4.0.

Optionally create a new realm:
```bash
./kcadm.sh create realms -s realm=spiffe -s enabled=true
```

```bash
./kcadm.sh create identity-provider/instances -r spiffe -f - << EOF
{
  "alias": "spiffe",
  "providerId": "spiffe",
  "hideOnLogin": true,
  "config": {
    "issuer": "spiffe://example.org",
    "bundleEndpoint": "https://localhost:8543"
  }
}
EOF
```

Then create a new client within the realm:
```bash
./kcadm.sh create clients -r spiffe  -f - << EOF
{
  "clientId": "myclient",
  "serviceAccountsEnabled": true,
  "clientAuthenticatorType": "federated-jwt",
  "attributes": {
    "jwt.credential.issuer": "spiffe"
    "jwt.credential.sub": "spiffe://example.org/myclient"    
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