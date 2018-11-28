#!/usr/bin/env bash

#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
DATE=$(date '+%m-%d-%Y-%H-%M-%S')

TAG=${1}
BRANCH=${2:-develop}
OUTPUT=${3:-output-${DATE}-${TAG}}
BENCHMARK_BRANCH=${4:-test_old_versions}
PREFIX="geode-performance-${TAG}"

INSTANCES=$(gcloud compute instance-groups list-instances ${PREFIX} | grep "${TAG}" | awk '{print $1}')


HOSTS=$(echo ${INSTANCES} | tr ' ' ',')

FIRST_INSTANCE=$(echo ${INSTANCES} | awk '{print $1}' )

gcloud compute ssh geode@$FIRST_INSTANCE --command="\
  rm -rf geode-benchmarks geode && \
  git clone --depth=1 https://github.com/apache/geode --branch ${BRANCH} geode && \
  git clone https://github.com/smgoller/geode-benchmarks --branch ${BENCHMARK_BRANCH} && \
  cd geode && \
  ./gradlew build -x test && \
  cd ../geode-benchmarks && \
  ./gradlew --include-build ../geode benchmark -Phosts=${HOSTS}"


mkdir -p ${OUTPUT}

gcloud compute scp --recurse geode@${FIRST_INSTANCE}:geode-benchmarks/geode-benchmarks/build/reports ${OUTPUT}/reports

gcloud compute scp --recurse geode@${FIRST_INSTANCE}:geode-benchmarks/geode-benchmarks/build/benchmarks ${OUTPUT}
