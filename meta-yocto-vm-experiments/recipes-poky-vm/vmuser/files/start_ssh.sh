#!/bin/sh

export PSEUDO_PREFIX=/usr
export PSEUDO_LOCALSTATEDIR=/home/vmuser/pseudo
export PSEUDO_LIBDIR=/usr/lib/pseudo/lib64
export GIT_PROXY_COMMAND=/home/vmuser/poky/scripts/oe-git-proxy

uid="$(id -u vmuser)"
gid="$(id -g vmuser)"
#ip="jbystric-mobl.amr.corp.intel.com"

#sudo mount -t cifs -o rw,uid=$uid,gid=$gid,username=<username> //$ip/shared-poky-vm /mnt/shared-poky-vm
