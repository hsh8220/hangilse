package com.erp.hangilse.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private long id;

    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    private String name;
    @JsonIgnore
    private boolean activated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "account_authority",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @Builder
    public Account(String email, String password, String name, Set<Authority> authorities) {
        Assert.notNull(email,"Id Not Null");
        Assert.notNull(password,"Password Not Null");
        Assert.notNull(name, "Name Not Null");
        Assert.notNull(authorities, "Authorities Not Null");

        this.email = email;
        this.password = password;
        this.name = name;
        this.authorities = authorities;
    }

}
