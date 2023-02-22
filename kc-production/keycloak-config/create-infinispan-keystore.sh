#!/bin/bash -e

keytool -genseckey -alias myKey -keyalg aes -keysize 128 -keystore conf/myKeystore.p12 -storetype PKCS12 -storepass changeit -keypass changeit