package com.ft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.ft.domain.enumeration.ReviewState;

/**
 * A LeaveRequest.
 */
@Entity
@Table(name = "leave_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LeaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "note")
    private String note;

    @Column(name = "all_day")
    private Boolean allDay;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "jhi_number")
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ReviewState state;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Lob
    @Column(name = "approval_note")
    private String approvalNote;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User approvedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LeaveRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public LeaveRequest note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isAllDay() {
        return allDay;
    }

    public LeaveRequest allDay(Boolean allDay) {
        this.allDay = allDay;
        return this;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public LeaveRequest startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public LeaveRequest endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getNumber() {
        return number;
    }

    public LeaveRequest number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ReviewState getState() {
        return state;
    }

    public LeaveRequest state(ReviewState state) {
        this.state = state;
        return this;
    }

    public void setState(ReviewState state) {
        this.state = state;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LeaveRequest updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getApprovalNote() {
        return approvalNote;
    }

    public LeaveRequest approvalNote(String approvalNote) {
        this.approvalNote = approvalNote;
        return this;
    }

    public void setApprovalNote(String approvalNote) {
        this.approvalNote = approvalNote;
    }

    public User getOwner() {
        return owner;
    }

    public LeaveRequest owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public LeaveRequest approvedBy(User user) {
        this.approvedBy = user;
        return this;
    }

    public void setApprovedBy(User user) {
        this.approvedBy = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LeaveRequest leaveRequest = (LeaveRequest) o;
        if (leaveRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leaveRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", note='" + getNote() + "'" +
            ", allDay='" + isAllDay() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", number=" + getNumber() +
            ", state='" + getState() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", approvalNote='" + getApprovalNote() + "'" +
            "}";
    }
}
