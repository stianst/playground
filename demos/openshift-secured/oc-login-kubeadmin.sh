#!/bin/bash -e

eval $(crc console --credentials | grep kubeadmin | cut -f 2 -d "'")
