#!/usr/bin/env bash
set -x
CERTBOT_HOSTNAME=$(curl http://metadata.google.internal/computeMetadata/v1/instance/attributes/certbot-hostname -H
"Metadata-Flavor: Google")
echo "Running certbot..."
echo "HOSTNAME is ${CERTBOT_HOSTNAME}"
certbot certonly -n --dns-google --agree-tos --email sgoller@pivotal.io -d ${CERTBOT_HOSTNAME}                    

