package com.assist.messaging.service.consumer;

public class GrievanceCreatedEvent {

    private String grievanceNo;
    private String grievanceTxnNo;
    private String email;
    private String name;
    private String createdAt;

    public GrievanceCreatedEvent() {
    }

    public String getGrievanceNo() {
        return grievanceNo;
    }

    public void setGrievanceNo(String grievanceNo) {
        this.grievanceNo = grievanceNo;
    }

    public String getGrievanceTxnNo() {
        return grievanceTxnNo;
    }

    public void setGrievanceTxnNo(String grievanceTxnNo) {
        this.grievanceTxnNo = grievanceTxnNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GrievanceCreatedEvent{" +
                "grievanceNo='" + grievanceNo + '\'' +
                ", grievanceTxnNo='" + grievanceTxnNo + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
