Admin UI test debug:
```
pnpm --filter @keycloak/keycloak-admin-ui test:integration -- --project=chromium --ui spiffe.spec.ts
```


Run a single Admin UI test:
```
pnpm --filter @keycloak/keycloak-admin-ui test:integration -- --project=chromium --ui spiffe.spec.ts
```

Playwright installation on Fedora (complains about missing deps, but doesn't matter):
```
sudo npx playwright install
```

Run lint:
```
pnpm --fail-if-no-match --filter @keycloak/keycloak-admin-ui lint -- --quiet
```

Run lint with fix:
```
pnpm --fail-if-no-match --filter @keycloak/keycloak-admin-ui lint -- --quiet --fix
```
