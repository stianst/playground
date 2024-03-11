#!/bin/bash

rm -f localhost-unsigned.key localhost-unsigned.crt unsigned-truststore

openssl req -x509 -newkey rsa:4096 -sha256 -days 3650   -nodes -keyout localhost-unsigned.key -out localhost-unsigned.crt -subj "/CN=localhost"   -addext "subjectAltName=DNS:localhost,DNS:*.localdomain,IP:127.0.0.1"

keytool -importcert -file localhost-unsigned.crt -keystore unsigned-truststore -noprompt --storepass password

echo ""
echo "Certificates created"
echo "--------------------"
echo "Public cert:         " $(readlink -f localhost-unsigned.crt)
echo "Private key:         " $(readlink -f localhost-unsigned.key)
echo "Truststore:          " $(readlink -f unsigned-truststore)
echo "Truststore password:  password"
