package com.erp.hangilse.project.domain;

import com.erp.hangilse.account.domain.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @Column(nullable = false, length = 1000)
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "project__id")
    private Project project;

    @Builder
    public Comment(LocalDateTime createTime, LocalDateTime updateTime,
                   String contents, List<Comment> children, Account account, Project project) {

        Assert.notNull(contents, "contents Not Null");
        Assert.notNull(account, "account Not Null");
        Assert.notNull(project, "project Not Null");

        this.createTime = createTime;
        this.updateTime = updateTime;
        this.contents = contents;
        this.children = children;
        this.account = account;
        this.project = project;
    }
}
