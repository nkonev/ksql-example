package com.example.kafkotest;

import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.KsqlObject;
import io.confluent.ksql.api.client.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Client ksqlClient;

    @PutMapping
    @PostMapping
    public void postUser(@RequestBody UserDto userDto) throws ExecutionException, InterruptedException {
        KsqlObject row = new KsqlObject()
                .put("USERID", userDto.userId())
                .put("REGISTERTIME", userDto.registertime())
                .put("GENDER", userDto.gender())
                .put("REGIONID", userDto.regionid());

        ksqlClient.insertInto("USERS", row).get();
    }

    @GetMapping
    public List<UserDto> findUser(@RequestParam(required = false) List<Long> userId) throws ExecutionException, InterruptedException {
        final String ksqlQuery;
        if (userId == null || userId.isEmpty()) {
            ksqlQuery = "SELECT * from QUERYABLE_USERS;";
        } else {
            String idsByComma = userId.stream().map(String::valueOf).collect(Collectors.joining(","));
            ksqlQuery = String.format("SELECT * from QUERYABLE_USERS WHERE userid in (%s);", idsByComma);
        }
        BatchedQueryResult batchedQueryResult = ksqlClient.executeQuery(ksqlQuery);
        List<Row> rows = batchedQueryResult.get();
        return rows.stream().map(row -> new UserDto(
                row.getLong("USERID"),
                Optional.ofNullable(row.getLong("REGISTERTIME")).map(aLong -> LocalDateTime.ofInstant(Instant.ofEpochMilli(aLong), ZoneId.systemDefault())).orElse(null),
                row.getString("GENDER"),
                row.getString("REGIONID")
        )).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id")Long id) throws ExecutionException, InterruptedException {
        KsqlObject row = new KsqlObject()
                .put("USERID", id)
                .put("DUMMY", (String)null);

        ksqlClient.insertInto("USERS_DELETED", row).get();
    }
}

