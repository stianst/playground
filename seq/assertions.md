## Assertion Framework for OAuth 2.0 Client Authentication and Authorization Grants

### Client action on behalf of user
```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    participant C as Application
    participant TS as Token Service
    participant KC as Keycloak
    participant RS as REST API
    C->>TS: Request assertion
    TS->>C: Assertion
    C->>KC: Token request, with assertion
    KC->>C: Token response
    C->>RS: Request with token
```

### Client action on behalf of itself
```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    participant C as Application
    participant TS as Token Service
    participant KC as Keycloak
    participant RS as REST API
    C->>TS: Request assertion
    TS->>C: Assertion
    C->>KC: Client credential request, with client_assertion
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
    participant IDPA as External IdP
    participant C as Client
    participant IDPB as Keycloak
    participant RS as REST API
    C->>IDPA: Exchange token
    IDPA->>C: Assertion
    C->>IDPB: Token request, with assertion
    IDPB->>C: Token response
    C->>RS: Request with token
```

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
    IDPA->>C: Assertion
    C->>IDPB: Token request, with assertion
    IDPB->>C: Token response
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
    C->>IDPB: Token exchange request
    IDPB->>C: Token response
    C->>RS: Request with token
```
