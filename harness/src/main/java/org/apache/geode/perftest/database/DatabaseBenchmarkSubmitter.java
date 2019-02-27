package org.apache.geode.perftest.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class DatabaseBenchmarkSubmitter {
  String baselineVersion;
  JSONObject metadata;
  List tests;
  String instanceId;

  public DatabaseBenchmarkSubmitter(File benchmarkDir) throws IOException {
    if (!benchmarkDir.exists()) {
      System.out.println("Unable to find test result directory: " + benchmarkDir.getName());
      System.exit(1);
    }

    String content = "";
    try
    {
      content = new String ( Files.readAllBytes( Paths.get(benchmarkDir.getAbsolutePath() + "/metadata.json") ) );
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }


    metadata = new JSONObject(content);
    tests = Files.list(benchmarkDir.toPath()).filter(path -> path.getFileName().toString().startsWith("org.apache.geode.benchmark.tests.") && path.toFile().isDirectory()).collect(
        Collectors.toList());
    instanceId = metadata.getString("instance_id");
    baselineVersion = metadata.getString("baseline_version");
  }
}
