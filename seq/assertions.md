## Assertion Framework for OAuth 2.0 Client Authentication and Authorization Grants
```mermaid
sequenceDiagram
    participant C as Application
    participant TS as Token Service
    participant KC as Keycloak
    participant RS as REST API
    C->>TS: Request assertion
    TS->>C: Assertion
    C->>KC: Token request
    KC->>C: Token response
    C->>RS: Request with token
```
