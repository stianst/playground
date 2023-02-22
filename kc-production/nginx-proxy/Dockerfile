FROM docker.io/nginx

ARG conf_file
COPY $conf_file /etc/nginx/conf.d/default.conf

RUN apt install openssl

RUN openssl req -x509 -nodes \
   -days 365 \
   -subj "/C=CA/ST=QC/O=Company, Inc./CN=localhost.localdomain" \
   -addext "subjectAltName=DNS:localhost.localdomain" \
   -newkey rsa:2048 \
   -keyout /etc/ssl/private/nginx-selfsigned.key \
   -out /etc/ssl/certs/nginx-selfsigned.crt;
