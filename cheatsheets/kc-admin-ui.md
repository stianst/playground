# Setup

Playwright installation on Fedora (complains about missing deps, but doesn't matter):
```
sudo pip install playwright==1.8.0a1
sudo python -m playwright install
```

# Development

Start Keycloak with the `KC_ADMIN_VITE_URL=http://localhost:5174` environment variable set, then run

```
pnpm --filter keycloak-admin-ui run dev
```

Now you can open the Keycloak admin console at `http://localhost:8080` and just refresh the admin console to get live code changes applied.

# Testing

Admin UI test debug:
```
pnpm --filter @keycloak/keycloak-admin-ui test:integration -- --project=chromium --ui spiffe.spec.ts
```

Run a single Admin UI test:
```
pnpm --filter @keycloak/keycloak-admin-ui test:integration -- spiffe.spec.ts
```

# Linting

Run lint:
```
pnpm --fail-if-no-match --filter @keycloak/keycloak-admin-ui lint -- --quiet
```

Run lint with fix:
```
pnpm --fail-if-no-match --filter @keycloak/keycloak-admin-ui lint -- --quiet --fix
```
