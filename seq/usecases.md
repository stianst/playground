# Use-Cases

## Mobile Sign in with AppleID

Mobile app uses sign in with AppleID and wants to access REST APIs, without requiring a separate login.

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

External applications uses a different IdP to internal REST APIs.

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

## SaaS login through Company IdP

A SaaS application is utilised by a company that wants to allow users to authenticate via their own IdP, but also allow internal applications to use REST APIs provided by the SaaS provider without additional logins.

```mermaid
---
config:
    mirrorActors: false
---
sequenceDiagram
    participant A as Internal App
    participant IDP as Company IdP
    participant SAS as SaaS Authorization Server
    participant SA as SaaS App
    participant SRS as SaaS Resource Server

    Note over A,IDP: User logs in to Internal App
    A->>IDP: Login request
    IDP->>A: Login response
    Note over SA,IDP: User logs in to SaaS App
    SA->>SAS: Login
    SAS->>IDP: Login (federated)
    IDP->>SAS: Login response
    SAS->>SA: Login response
    Note over A,SRS: Internal app access SaaS Resource Server
    A->>SAS: Get token
    SAS->>A: Return token
    A->>SRS: Request
```
