package com.test.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.test.enums.Status;

import javax.persistence.*;
import java.util.List;


@Entity
public class User extends AbstractModel {

    private long verificationTime;
    private long resetPasswordTime;

    @Column(unique = true)
    private String verificationCode;

    @Column(name = "resetPasswordCode", unique = true)
    private String resetPasswordCode;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ManyToMany
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authority;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Painter painter;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Wallet wallet;


    public List<Authority> getAuthority() {
        return authority;
    }

    public void setAuthority(List<Authority> authority) {
        this.authority = authority;
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter painter) {
        this.painter = painter;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public long getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(long verificationTime) {
        this.verificationTime = verificationTime;
    }

    public long getResetPasswordTime() {
        return resetPasswordTime;
    }

    public void setResetPasswordTime(long resetPasswordTime) {
        this.resetPasswordTime = resetPasswordTime;
    }

    public String getResetPasswordCode() {
        return resetPasswordCode;
    }

    public void setResetPasswordCode(String resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "verificationCode='" + verificationCode + '\'' +
                ", verificationTime=" + verificationTime +
                ", resetPasswordTime=" + resetPasswordTime +
                ", resetPasswordCode='" + resetPasswordCode + '\'' +
                ", status=" + status +
                ", authority=" + authority +
                ", painter=" + painter +
                ", wallet=" + wallet +
                '}';
    }
}
