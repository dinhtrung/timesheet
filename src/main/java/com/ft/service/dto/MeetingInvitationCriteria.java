package com.ft.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the MeetingInvitation entity. This class is used in MeetingInvitationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /meeting-invitations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MeetingInvitationCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private ZonedDateTimeFilter decidedAt;

    private BooleanFilter accepted;

    private LongFilter meetingId;

    public MeetingInvitationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getDecidedAt() {
        return decidedAt;
    }

    public void setDecidedAt(ZonedDateTimeFilter decidedAt) {
        this.decidedAt = decidedAt;
    }

    public BooleanFilter getAccepted() {
        return accepted;
    }

    public void setAccepted(BooleanFilter accepted) {
        this.accepted = accepted;
    }

    public LongFilter getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(LongFilter meetingId) {
        this.meetingId = meetingId;
    }

    @Override
    public String toString() {
        return "MeetingInvitationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (decidedAt != null ? "decidedAt=" + decidedAt + ", " : "") +
                (accepted != null ? "accepted=" + accepted + ", " : "") +
                (meetingId != null ? "meetingId=" + meetingId + ", " : "") +
            "}";
    }

}
