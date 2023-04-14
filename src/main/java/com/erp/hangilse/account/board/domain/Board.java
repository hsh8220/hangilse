package com.erp.hangilse.account.board.domain;

import com.erp.hangilse.account.domain.Account;
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
public class Board {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private long id;
    private String type;
    private BoardLevel level;
    private LocalDate createTime;
    private LocalDate updateTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acoount_id")
    private Account account;
    @Column(length = 2000)
    private String contents;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "board_tag_table",
            joinColumns = {@JoinColumn(name = "board_id", referencedColumnName = "board_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "tag_id")})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Tag> tags;

    @Builder
    public Board (String type, BoardLevel level, LocalDate createTime, LocalDate updateTime,
                 Account account, String contents, Set<Tag> tags) {

        Assert.notNull(type, "Type Not Null");
        Assert.notNull(level, "Level Not Null");
        Assert.notNull(account, "Account Not Null");

        this.type = type;
        this.level = level;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.account = account;
        this.contents = contents;
        this.tags = tags;
    }
}
