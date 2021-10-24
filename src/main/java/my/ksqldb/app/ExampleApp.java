package my.ksqldb.app;

import io.confluent.ksql.api.client.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExampleApp {

  public static String KSQLDB_SERVER_HOST = "localhost";
  public static int KSQLDB_SERVER_HOST_PORT = 8088;

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ClientOptions options = ClientOptions.create()
        .setHost(KSQLDB_SERVER_HOST)
        .setPort(KSQLDB_SERVER_HOST_PORT);
    Client client = Client.create(options);

    // Send requests with the client by following the other examples
    /*StreamedQueryResult streamedQueryResult = client.streamQuery("SELECT * FROM transactions EMIT CHANGES;").get();

    for (int i = 0; i < 10; i++) {
      // Block until a new row is available
      Row row = streamedQueryResult.poll();
      if (row != null) {
        System.out.println("Received a row!");
        System.out.println("Row: " + row.values());
      } else {
        System.out.println("Query has ended.");
      }
    }*/
    String pullQuery = "SELECT * FROM QUERYABLE_GRADES WHERE ID = 1;";
    BatchedQueryResult batchedQueryResult = client.executeQuery(pullQuery);

// Wait for query result
    List<Row> resultRows = batchedQueryResult.get();

    System.out.println("Received results. Num rows: " + resultRows.size());
    for (Row row : resultRows) {
      System.out.println("Row: " + row.values());
    }

    // Terminate any open connections and close the client
    client.close();
  }
}
