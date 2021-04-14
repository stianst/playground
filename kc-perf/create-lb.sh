#!/bin/bash -e

curl -o /tmp/wildfly.zip https://download.jboss.org/wildfly/23.0.0.Final/wildfly-23.0.0.Final.zip
unzip /tmp/wildfly.zip
mv wildfly* lb

lb/bin/jboss-cli.sh <<EOF
embed-server

/subsystem=undertow/configuration=handler/reverse-proxy=my-handler:add()

/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=remote-host1/:add(host=localhost, port=8180)
/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=remote-host2/:add(host=localhost, port=8280)
/socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=remote-host3/:add(host=localhost, port=8380)

/subsystem=undertow/configuration=handler/reverse-proxy=my-handler/host=host1:add(outbound-socket-binding=remote-host1, scheme=http, instance-id=myroute, path=/auth)
/subsystem=undertow/configuration=handler/reverse-proxy=my-handler/host=host2:add(outbound-socket-binding=remote-host2, scheme=http, instance-id=myroute, path=/auth)
/subsystem=undertow/configuration=handler/reverse-proxy=my-handler/host=host3:add(outbound-socket-binding=remote-host3, scheme=http, instance-id=myroute, path=/auth)

/subsystem=undertow/server=default-server/host=default-host/location=\/auth:add(handler=my-handler)

stop-embedded-server
EOF
