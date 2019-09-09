package com.bcauction.domain;

import java.time.LocalDateTime;

public class Member {
    private long id;
    private String 이름;
    private String 이메일;
    private String 비밀번호;
    private LocalDateTime 등록일시;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String get이름() {
        return 이름;
    }

    public void set이름(String 이름) {
        this.이름 = 이름;
    }

    public String get이메일() {
        return 이메일;
    }

    public void set이메일(String 이메일) {
        this.이메일 = 이메일;
    }

    public LocalDateTime get등록일시() {
        return 등록일시;
    }

    public void set등록일시(LocalDateTime 등록일시) {
        this.등록일시 = 등록일시;
    }

    public String get비밀번호()
    {
        return 비밀번호;
    }

    public void set비밀번호(final String 비밀번호)
    {
        this.비밀번호 = 비밀번호;
    }
}
