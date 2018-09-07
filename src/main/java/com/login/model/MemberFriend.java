package com.login.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description: MemberFriend.java
 *
 * @author TrinhNX
 * @create Sep 5, 2018
 */
@Entity
@Table(name = "member_friend")
public class MemberFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int ownerCode;
    private int fromMemberCode;
    private int toMemberCode;
    private int requestType;
    private int requestStatus;
    private String requestMessage;
    private LocalDateTime sendAt;

    public int getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(int ownerCode) {
        this.ownerCode = ownerCode;
    }

    public int getFromMemberCode() {
        return fromMemberCode;
    }

    public void setFromMemberCode(int fromMemberCode) {
        this.fromMemberCode = fromMemberCode;
    }

    public int getToMemberCode() {
        return toMemberCode;
    }

    public void setToMemberCode(int toMemberCode) {
        this.toMemberCode = toMemberCode;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

}
