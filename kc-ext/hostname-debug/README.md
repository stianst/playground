# Extension to help debug hostname issues

1. Build with `mvn clean install`
2. Copy `target/hostname-debug.jar` to `KC_HOME/providers/`
3. Start Keycloak
4. Open `KEYCLOAK_PUBLIC_URL/realms/master/hostname-debug/`