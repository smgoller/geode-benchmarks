#!/usr/bin/env bash
set -x
CERTBOT_HOSTNAME=$(curl http://metadata.google.internal/computeMetadata/v1/instance/attributes/certbot-hostname -H "Metadata-Flavor: Google")
echo "Running certbot..."
echo "HOSTNAME is ${CERTBOT_HOSTNAME}"
pip3 install -U certbot-dns-google
pip3 install -U certbot
certbot certonly -n --dns-google --dns-google-propagation-seconds=300 --agree-tos --email sgoller@pivotal.io -d ${CERTBOT_HOSTNAME}

