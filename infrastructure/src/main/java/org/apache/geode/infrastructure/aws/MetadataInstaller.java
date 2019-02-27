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

package org.apache.geode.infrastructure.aws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import net.schmizz.sshj.Config;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.FileAttributes;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FilePermission;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.json.JSONObject;


public class MetadataInstaller {
  public static final Config CONFIG = new DefaultConfig();
  private static final int RETRIES = 30;
  private final String user;
  private final Path Metadata;

  public MetadataInstaller(String user, Path Metadata) {
    this.user = user;
    this.Metadata = Metadata;
  }


  public void installMetadata(Collection<String> hosts) {
    JSONObject metadataJSON = generateMetadata();
    hosts.forEach(host -> installMetadata(host, metadataJSON));
  }

  public JSONObject generateMetadata() {
    JSONObject metadataJSON = new JSONObject();
    metadataJSON.put("instance_id", UUID.randomUUID().toString());

    return metadataJSON;
  }

  private void installMetadata(String host, JSONObject metadataJSON) {
    try (SSHClient client = new SSHClient(CONFIG)) {
      client.addHostKeyVerifier(new PromiscuousVerifier());
      connect(host, client);
      client.authPublickey(user, Metadata.toFile().getAbsolutePath());
      SFTPClient sftpClient = client.newSFTPClient();
      String dest = "/home/" + user + "/.geode-benchmarks/metadata.json";
      // Create temp file.
      File temp = File.createTempFile("benchmarks-metadata", ".json");

      // Delete temp file when program exits.
      temp.deleteOnExit();

      // Write to temp file
      BufferedWriter out = new BufferedWriter(new FileWriter(temp));
      out.write("aString");
      out.close();
      sftpClient.put(new FileSystemFile(temp), dest);
      sftpClient.setattr(dest, new FileAttributes.Builder()
          .withPermissions(Collections.singleton(FilePermission.USR_R)).build());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void connect(String host, SSHClient client) throws IOException, InterruptedException {

    int i = 0;
    while (true) {
      try {
        i++;
        client.connect(host);
        return;
      } catch (ConnectException e) {
        if (i >= RETRIES) {
          throw e;
        }

        System.out.println("Failed to connect, retrying...");
        Thread.sleep(AwsBenchmarkMetadata.POLL_INTERVAL);
      }
    }
  }
}
