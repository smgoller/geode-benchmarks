package org.apache.geode.perftest.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Submitter {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println(
          "Database submitter takes one test output directory as an argument");
      System.exit(1);
    }

    String benchmarkDirArg = args[0];

    File benchmarkDir = new File(benchmarkDirArg);

    if (!benchmarkDir.exists()) {
      System.out.println("Unable to find test result directory: " + benchmarkDirArg);
      System.exit(1);
    }

    System.out.println("Submitting benchmark result");
    /*
    metadata.json - build information
    each test -
      latency_csv.hgrm
      ThroughputLatencyProbe.csv
     */
    DatabaseBenchmarkSubmitter dbs = new DatabaseBenchmarkSubmitter(benchmarkDir);
    System.out.println("we got here");
  }
}
