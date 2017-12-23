package com.ft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.ft.domain.enumeration.ReviewState;

/**
 * A Timesheet.
 */
@Entity
@Table(name = "timesheet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timesheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Lob
    @Column(name = "approval_note")
    private String approvalNote;

    @Max(value = 9999)
    @Column(name = "jhi_year")
    private Integer year;

    @Column(name = "week")
    private Integer week;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ReviewState state;

    @Lob
    @Column(name = "jhi_comment")
    private String comment;

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

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public Timesheet approvedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
        return this;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getApprovalNote() {
        return approvalNote;
    }

    public Timesheet approvalNote(String approvalNote) {
        this.approvalNote = approvalNote;
        return this;
    }

    public void setApprovalNote(String approvalNote) {
        this.approvalNote = approvalNote;
    }

    public Integer getYear() {
        return year;
    }

    public Timesheet year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeek() {
        return week;
    }

    public Timesheet week(Integer week) {
        this.week = week;
        return this;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public ReviewState getState() {
        return state;
    }

    public Timesheet state(ReviewState state) {
        this.state = state;
        return this;
    }

    public void setState(ReviewState state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public Timesheet comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getOwner() {
        return owner;
    }

    public Timesheet owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public Timesheet approvedBy(User user) {
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
        Timesheet timesheet = (Timesheet) o;
        if (timesheet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timesheet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Timesheet{" +
            "id=" + getId() +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", approvalNote='" + getApprovalNote() + "'" +
            ", year=" + getYear() +
            ", week=" + getWeek() +
            ", state='" + getState() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
