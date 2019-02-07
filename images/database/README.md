Build requirements:
* Packer
* Ansible

  — name: Install list of packages
    apt: pkg="apt-transport-https" state=present update_cache=yes cache_valid_time=604800
    become: yes

    with_items:
      - apt-transport-https
      - certbot
      - python3-certbot-dns-google
      - python3-certbot-dns-route53
      - postgresql


"sudo certbot certonly --dry-run -n --dns-google --agree-tos --email sgoller@pivotal.io -d postgres-test3.srv.apachegeode-ci.info"


#!/usr/bin/env bash
set -x
echo "attempting to get cert"
certbot certonly --dry-run -n --dns-google --agree-tos --email sgoller@pivotal.io -d postgres-test6.srv.apachegeode-ci.info --dns-google-propagation-seconds 120