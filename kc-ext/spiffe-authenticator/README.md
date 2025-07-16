## Setting up Spire Server and Agent

Start the Spire Server:

```shell
./start-spire-server.sh
```

Start the Spire Agent:

```shell
./start-spire-agent.sh
```

Register the workload:

```shell
./register-workload.sh
```

Retrieve a JWT-SVID:

```shell
./retrieve-jwt-svid.sh
```

Keys are saved to `target/tmp/spire/keys.json`

SPIFFE ID is `spiffe://example.org/myclient`

Start SPIRE OIDC Discovery Provider:

```shell
./start-spire-oidc.sh
```

Decode and verify token with `kct`:

```shell
./decode-jwt-svid.sh
```
