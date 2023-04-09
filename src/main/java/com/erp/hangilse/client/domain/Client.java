package com.erp.hangilse.client.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue
    @Column(name = "client_id")
    private long id;

    private String name;

    private String type;

    private LocalDate createTime;

    private String address;
}
