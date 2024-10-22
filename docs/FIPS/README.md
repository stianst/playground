Add the following from https://www.bouncycastle.org/download/bouncy-castle-java-fips/#latest to `$KC_HOME/providers/`:
```
bc-fips-2.0.0.jar
bcpkix-fips-2.0.7.jar
bctls-fips-2.0.19.jar
bcutil-fips-2.0.3.jar
```

Download Java from `https://adoptium.net/` and update `conf/security/java.security` file to have the following providers:

```
security.provider.1=org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider
security.provider.2=org.bouncycastle.jsse.provider.BouncyCastleJsseProvider fips:BCFIPS
security.provider.3=sun.security.provider.Sun 
```

Note: Without `sun.security.provider.Sun` Keycloak doesn't start

Generate a self-signed certificate for Keycloak:
```
cd $KC_HOME/conf
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 3650 -nodes -subj "/CN=localhost"
```

Start Keycloak with:
```
bin/kc.sh start --features=fips --https-certificate-file=conf/cert.pem --https-certificate-key-file=conf/key.pem --hostname-strict=false
```

### SHA-1 not available

Without `sun.security.provider.Sun ` the following error is thrown on startup:

```
Exception in thread "main" java.lang.reflect.InvocationTargetException
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:118)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at io.quarkus.bootstrap.runner.QuarkusEntryPoint.doRun(QuarkusEntryPoint.java:62)
	at io.quarkus.bootstrap.runner.QuarkusEntryPoint.main(QuarkusEntryPoint.java:33)
Caused by: java.lang.InternalError: internal error: SHA-1 not available.
	at java.base/sun.security.provider.SecureRandom.init(SecureRandom.java:116)
	at java.base/sun.security.provider.SecureRandom.<init>(SecureRandom.java:87)
	at java.base/java.security.SecureRandom.getDefaultPRNG(SecureRandom.java:293)
	at java.base/java.security.SecureRandom.<init>(SecureRandom.java:225)
	at java.base/java.util.UUID$Holder.<clinit>(UUID.java:104)
	at java.base/java.util.UUID.randomUUID(UUID.java:150)
	at io.quarkus.runtime.configuration.ConfigUtils.configBuilder(ConfigUtils.java:67)
	at io.quarkus.runtime.configuration.QuarkusConfigFactory.getConfigFor(QuarkusConfigFactory.java:27)
	at io.smallrye.config.SmallRyeConfigProviderResolver.getConfig(SmallRyeConfigProviderResolver.java:78)
	at io.smallrye.config.SmallRyeConfigProviderResolver.getConfig(SmallRyeConfigProviderResolver.java:66)
	at org.keycloak.quarkus.runtime.configuration.Configuration.getConfig(Configuration.java:79)
	at org.keycloak.quarkus.runtime.configuration.Configuration.getBuildTimeProperty(Configuration.java:101)
	at org.keycloak.quarkus.runtime.cli.Picocli.requiresReAugmentation(Picocli.java:250)
	at org.keycloak.quarkus.runtime.cli.Picocli.runReAugmentationIfNeeded(Picocli.java:227)
	at org.keycloak.quarkus.runtime.cli.Picocli.parseAndRun(Picocli.java:132)
	at org.keycloak.quarkus.runtime.KeycloakMain.main(KeycloakMain.java:106)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	... 3 more
Caused by: java.security.NoSuchAlgorithmException: SHA MessageDigest not available
	at java.base/sun.security.jca.GetInstance.getInstance(GetInstance.java:159)
	at java.base/java.security.MessageDigest.getInstance(MessageDigest.java:185)
	at java.base/sun.security.provider.SecureRandom.init(SecureRandom.java:114)
	... 19 more
```
