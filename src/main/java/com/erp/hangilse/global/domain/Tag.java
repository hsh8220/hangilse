package com.erp.hangilse.global.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Tag {

    @Id
    @JsonIgnore
    @GeneratedValue
    @Column(name = "tag_id")
    private long id;

    private String name;

    @Builder
    public Tag(String name) {
        Assert.notNull(name, "Name Not Null");

        this.name = name;
    }
}
