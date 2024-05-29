package com.example.quily.query;


import org.springframework.stereotype.Component;

@Component
public class SQLQueries {

    public String updateIndicesIfIndicesIsGreater() {
       return  "UPDATE key_indices AS k " +
                "JOIN (SELECT id, index6 * 100000 + index5 * 10000 + index4 * 1000 + index3 * 100 + index2 * 10 + index1 AS current_value " +
                "FROM key_indices WHERE id = :id) AS sub " +
                "ON k.id = sub.id " +
                "SET k.index1 = :index1, k.index2 = :index2, k.index3 = :index3, k.index4 = :index4, k.index5 = :index5, k.index6 = :index6 " +
                "WHERE k.id = :id AND (:index6 * 100000 + :index5 * 10000 + :index4 * 1000 + :index3 * 100 + :index2 * 10 + :index1) > sub.current_value;";
    }
}
