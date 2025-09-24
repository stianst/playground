# Use-Cases

## Mobile Sign in with AppleID

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    box Domain A
    participant Apple as Apple
    participant App as App
    end
    box Domain B
    participant RS as REST API
    participant KC as Keycloak
    end
    App->>Apple: Login
    App->>RS: Request
    RS->>KC: Verify token
```

## Gateway

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    box Domain A
    participant IA as External IdP
    participant C as Client
    end
    box Domain B
    participant G as Gateway
    participant RS as REST API
    participant KC as Keycloak
    end
    C->>IA: Login
    C->>G: Request
    G->>RS: Request
    RS->>KC: Verify token
```
