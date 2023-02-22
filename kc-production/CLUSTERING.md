# Setup symmetric encryption for caches

# Create keystore

```
keytool -genseckey -alias myKey -keyalg aes -keysize 128 -keystore conf/myKeystore.p12 -storetype PKCS12 -storepass changeit -keypass changeit
```

# Edit `conf/cache-ispn.xml`

Add the following at the top:
```
  <jgroups>
    <stack name="encrypt-udp" extends="udp">
      <SYM_ENCRYPT keystore_name="conf/myKeystore.p12"
                   keystore_type="PKCS12"
                   store_password="changeit"
                   key_password="changeit"
                   alias="myKey"
                   stack.combine="INSERT_AFTER"
                   stack.position="VERIFY_SUSPECT"/>
    </stack>
  </jgroups>
```

Change `<transport lock-timeout="60000"/>` to `<transport lock-timeout="60000" stack="encrypt-udp"/>`