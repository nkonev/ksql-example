package com.example.kafkotest;

import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Client ksqlClient;

    @PutMapping
    @PostMapping
    public void postUser() {
    }

    @GetMapping
    public List<UserDto> findUser() throws ExecutionException, InterruptedException {
        BatchedQueryResult batchedQueryResult = ksqlClient.executeQuery("select * from QUERYABLE_USERS;");
        List<Row> rows = batchedQueryResult.get();
        return rows.stream().map(row -> new UserDto(
                row.getLong("USERID"),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(row.getLong("REGISTERTIME")), ZoneId.systemDefault()),
                row.getString("GENDER"),
                row.getString("REGIONID")
        )).collect(Collectors.toList());
    }

    @DeleteMapping
    public void deleteUser() {

    }
}

class UserDto {
    private Long userId;
    private LocalDateTime registertime;
    private String gender;
    private String regionid;

    public UserDto() {
    }

    public UserDto(Long userId, LocalDateTime registertime, String gender, String regionid) {
        this.userId = userId;
        this.registertime = registertime;
        this.gender = gender;
        this.regionid = regionid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getRegistertime() {
        return registertime;
    }

    public void setRegistertime(LocalDateTime registertime) {
        this.registertime = registertime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }
}