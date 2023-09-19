#!/bin/bash -e

eval $(crc console --credentials | grep developer | cut -f 2 -d "'")
