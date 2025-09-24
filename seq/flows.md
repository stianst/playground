## Assertion Framework for OAuth 2.0 Client Authentication and Authorization Grants

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    participant C as Client
    participant TS as Token Service
    participant KC as Keycloak
    participant RS as REST API

    C->>TS: Request assertion
    TS->>C: Assertion
    C->>KC: Token request, with assertion
    KC->>C: Token response
    C->>RS: Request with token
```

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    participant C as Client
    participant TS as Token Service
    participant KC as Keycloak
    participant RS as REST API

    C->>TS: Request client assertion
    TS->>C: Client Assertion
    C->>KC: Client credential grant, with client assertion
    KC->>C: Token response
    C->>RS: Request with token
```

## OAuth Identity and Authorization Chaining Across Domains

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    box Domain A
    participant IDPA as External IdP
    participant C as Client
    end
    box Domain B
    participant IDPB as Keycloak
    participant RS as REST API
    end

    C->>IDPA: Exchange token
    IDPA->>C: Assertion
    C->>IDPB: Token request, with assertion
    IDPB->>C: Token response
    C->>RS: Request with token
```

If the External IdP does not support token exchange, this can be delegated to a separate Token Service.

### With additional Token Service

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    participant IDPA as External IdP
    participant TS as Token Service
    participant C as Client
    participant IDPB as Keycloak
    participant RS as REST API
    C<<->>IDPA: Login
    C->>TS: Exchange token
    TS->>C: Assertion
    C->>IDPB: Token request, with assertion
    IDPB->>C: Token response
    C->>RS: Request with token
```

## Identity Assertion Authorization Grant

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    box Domain A
    participant E as External IdP
    participant C as Client
    end
    box Domain B
    participant KC as Keycloak
    participant RS as REST API
    end

    C->>E: Login
    E->>C: ID Token
    C->>E: Exchange token
    E->>C: ID-JAG
    C->>KC: Token request, with ID-JAG
    KC->>C: Token response
    C->>RS: Request with token
```

## Keycloak External to Internal Token Exchange

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    participant IDPA as External IdP
    participant C as Client
    participant IDPB as Keycloak
    participant RS as REST API
    C->>IDPA: Token exchange request, aud=keycloak
    IDPA->>C: Token response
    C->>IDPB: Token exchange request
    IDPB->>C: Token response
    C->>RS: Request with token
```

The problem with this pattern is the token exchange request has to include an subject_token with `aud=keycloak`, which it will not have:

* If client is in a chain of requests the token will have the client as the audience, not Keycloak. To obtain a token with audience including Keycloak it would have to make a separate request to the External IdP, which is what identity chaining does
* If the client is a frontend application it will be accessing both internal and external services; If it only had to access external services there is no need for a local IdP and the user can just login directly to Keycloak
