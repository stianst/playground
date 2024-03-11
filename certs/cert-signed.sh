#!/bin/bash -e

rm -f RootCA.crt RootCA.key RootCA.pem localhost-signed.key localhost-signed.csr localhost-signed.crt signed-truststore

openssl req -x509 -nodes -new -sha256 -days 1024 -newkey rsa:2048 -keyout RootCA.key -out RootCA.pem -subj "/C=US/CN=Example-Root-CA"
openssl x509 -outform pem -in RootCA.pem -out RootCA.crt

openssl req -new -nodes -newkey rsa:2048 -keyout localhost-signed.key -out localhost-signed.csr -subj "/C=US/ST=YourState/L=YourCity/O=Example-Certificates/CN=localhost"
openssl x509 -req -sha256 -days 1024 -in localhost-signed.csr -CA RootCA.pem -CAkey RootCA.key -CAcreateserial -extfile domains.ext -out localhost-signed.crt

keytool -importcert -file localhost-signed.crt -keystore signed-truststore -noprompt --storepass password

echo ""
echo "Certificates created"
echo "--------------------"
echo "CA cert:             " $(readlink -f RootCA.crt) 
echo "CA key:              " $(readlink -f RootCA.key)
echo "Public cert:         " $(readlink -f localhost-unsigned.crt)
echo "Private key:         " $(readlink -f localhost-unsigned.key)
echo "Truststore:          " $(readlink -f signed-truststore)
echo "Truststore password:  password"
