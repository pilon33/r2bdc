package com.example.r2dgbc.demo.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("car")
public class Car {

    @Id
    Integer id;

    String brand;

    Integer km;

    public static Car carFromRow(Map <String, Object> row) {
        if (row.get("c_id") != null) {
            return Car.builder()
                    .id((int) Long.parseLong(row.get("c_id").toString()))
                    .brand((String) row.get("brand"))
                    .km((Integer)  row.get("km"))
                    .build();
        } else {
            return null;
        }
    }

}
