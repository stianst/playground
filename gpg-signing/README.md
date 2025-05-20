# Creating keys

## GPG

```
gpg --generate-key
gpg --output gpg-priv.asc --armor --export-secret-key <KEY ID>
gpg --output gpg-pub.asc --armor --export <KEY ID>
```

## Sequoia

```
sq key generate --userid sqtest --name sqtest --email sqtest@localhost --own-key
sq key export --cert-userid=sqtest --output sq-priv.asc
sq cert export --cert-userid=sqtest --output sq-pub.asc
```

# BouncyCastle

```
mvn exec:java -Dexec.mainClass=org.bouncycastle.openpgp.examples.EllipticCurveKeyPairGenerator -Dexec.arguments="-a,bctest@local,123245678"  -Dexec.workingDir=.
mv secret.asc bc-priv.asc
mv pub.asc pc-pub.asc
```

# Signing using GPG Plugin 

```
export MAVEN_GPG_KEY=$(cat private-key-file)
export MAVEN_GPG_PASSPHRASE=12345678
mvn -Dgpg.signer=bc package gpg:sign
```

# Resources

* [Apache Maven GPG Plugin](https://maven.apache.org/plugins/maven-gpg-plugin/index.html)
* [Sequoia Documentation](https://book.sequoia-pgp.org/)