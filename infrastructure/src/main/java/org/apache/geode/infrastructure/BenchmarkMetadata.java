/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geode.infrastructure;

/**
 * Static methods to generate common strings used by the infrastructure.
 */
public class BenchmarkMetadata {
  public static String PREFIX = "geode-benchmarks";
  public static String SSH_DIRECTORY = ".ssh/geode-benchmarks";

  public static String benchmarkPrefix(String tag) {
    return PREFIX + "-" + tag;
  }

  public static String benchmarkString(String tag, String suffix) {
    return benchmarkPrefix(tag) + "-" + suffix;
  }

  public static String benchmarkKeyFileDirectory() {
    return System.getProperty("user.home") + "/" + SSH_DIRECTORY;
  }

  public static String benchmarkMetadataFileDirectory() {
    return benchmarkKeyFileDirectory();
  }

  public static String benchmarkKeyFileName(String tag) {
    return benchmarkKeyFileDirectory() + "/" + tag + ".pem";
  }

  public static String benchmarkMetadataFileName(String tag) {
    return benchmarkMetadataFileDirectory() + "/" + tag + "-metadata.json";
  }
}
