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
                .put("USERID", userDto.getUserId())
                .put("REGISTERTIME", userDto.getRegistertime())
                .put("GENDER", userDto.getGender())
                .put("REGIONID", userDto.getRegionid());

        ksqlClient.insertInto("USERS", row).get();
    }

    @GetMapping
    public List<UserDto> findUser() throws ExecutionException, InterruptedException {
        BatchedQueryResult batchedQueryResult = ksqlClient.executeQuery("select * from QUERYABLE_USERS;");
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
        //ksqlClient.executeStatement("INSERT INTO USERS (USERID, REGISTERTIME, GENDER, REGIONID) VALUES (1510923225000, 'key', 'A');").get();
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