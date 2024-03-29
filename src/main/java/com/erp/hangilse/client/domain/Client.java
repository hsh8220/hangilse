package com.erp.hangilse.client.domain;

import com.erp.hangilse.global.domain.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

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
    private LocalDate updateTime;
    private String address;
    @Column(length = 1000)
    private String contents;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "client_tag_table",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "client_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "tag_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Tag> tags;

    @Builder
    public Client (String name, String type, LocalDate createTime, LocalDate updateTime, String address, String contents, Set<Tag> tags) {
        Assert.notNull(name, "Name Not Null");
        Assert.notNull(type, "Type Not Null");

        this.name = name;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.address = address;
        this.contents = contents;
        this.tags = tags;
    }

}
