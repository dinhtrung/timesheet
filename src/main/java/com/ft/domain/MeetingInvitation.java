package com.ft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A MeetingInvitation.
 */
@Entity
@Table(name = "meeting_invitation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MeetingInvitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "decided_at")
    private ZonedDateTime decidedAt;

    @Column(name = "accepted")
    private Boolean accepted;

    @ManyToOne
    private Meeting meeting;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDecidedAt() {
        return decidedAt;
    }

    public MeetingInvitation decidedAt(ZonedDateTime decidedAt) {
        this.decidedAt = decidedAt;
        return this;
    }

    public void setDecidedAt(ZonedDateTime decidedAt) {
        this.decidedAt = decidedAt;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public MeetingInvitation accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public MeetingInvitation meeting(Meeting meeting) {
        this.meeting = meeting;
        return this;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
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
        MeetingInvitation meetingInvitation = (MeetingInvitation) o;
        if (meetingInvitation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), meetingInvitation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MeetingInvitation{" +
            "id=" + getId() +
            ", decidedAt='" + getDecidedAt() + "'" +
            ", accepted='" + isAccepted() + "'" +
            "}";
    }
}
