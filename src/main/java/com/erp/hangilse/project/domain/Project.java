package com.erp.hangilse.project.domain;

import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.client.domain.Client;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private long id;

    private String name;

    private String type;

    private StatusEnum status;

    private LocalDate createTime;

    private LocalDate completeTime;

    private LocalDate endTime;

    private LocalDate targetTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acoount_id")
    private Account account;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_watcher_table",
            joinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "account_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Account> watchers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_tag_table",
            joinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "tag_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Tag> tags;

    private Double cost;
    @Column(length = 2000)
    private String contents;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Project(String name, String type, StatusEnum status,
                   LocalDate createTime, LocalDate completeTime, LocalDate endTime, LocalDate targetTime,
                   Client client, Account account, Set<Account> watchers,
                   Double cost, String contents) {
        Assert.notNull(name, "Name Not Null");
        Assert.notNull(type, "Type Not Null");
        Assert.notNull(status, "Status Not Null");

        this.name = name;
        this.type = type;
        this.status = status;
        this.createTime = createTime;
        this.completeTime = completeTime;
        this.endTime = endTime;
        this.targetTime = targetTime;
        this.client = client;
        this.account = account;
        this.watchers = watchers;
        this.cost = cost;
        this.contents = contents;
    }
}
