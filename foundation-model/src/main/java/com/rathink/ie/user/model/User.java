package com.rathink.ie.user.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    private String id;
    private String name;
    private String password;
    private String wechat;
    private String userTeamPosition;
    private String userLevel;
    private String sourceType;//wechat code others
    private User sourceUser;
    private Integer sourceLevel;
    private String sourceCode;

    @Id
    @GenericGenerator(name = "id", strategy = "com.ming800.core.p.model.M8idGenerator")
    @GeneratedValue(generator = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "wechat")
    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    @Column(name = "user_team_position")
    public String getUserTeamPosition() {
        return userTeamPosition;
    }

    public void setUserTeamPosition(String userTeamPosition) {
        this.userTeamPosition = userTeamPosition;
    }

    @Column(name = "user_level")
    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    @Column(name = "source_type")
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    @Column(name = "source_level")
    public Integer getSourceLevel() {
        return sourceLevel;
    }

    public void setSourceLevel(Integer sourceLevel) {
        this.sourceLevel = sourceLevel;
    }

    @Column(name = "source_code")
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
