package com.ft.service.dto;

import java.io.Serializable;
import com.ft.domain.enumeration.ReviewState;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Timesheet entity. This class is used in TimesheetResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /timesheets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TimesheetCriteria implements Serializable {
    /**
     * Class for filtering ReviewState
     */
    public static class ReviewStateFilter extends Filter<ReviewState> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private ZonedDateTimeFilter approvedAt;

    private IntegerFilter year;

    private IntegerFilter week;

    private ReviewStateFilter state;

    private LongFilter ownerId;

    private LongFilter approvedById;

    public TimesheetCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTimeFilter approvedAt) {
        this.approvedAt = approvedAt;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public IntegerFilter getWeek() {
        return week;
    }

    public void setWeek(IntegerFilter week) {
        this.week = week;
    }

    public ReviewStateFilter getState() {
        return state;
    }

    public void setState(ReviewStateFilter state) {
        this.state = state;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(LongFilter approvedById) {
        this.approvedById = approvedById;
    }

    @Override
    public String toString() {
        return "TimesheetCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (approvedAt != null ? "approvedAt=" + approvedAt + ", " : "") +
                (year != null ? "year=" + year + ", " : "") +
                (week != null ? "week=" + week + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
                (approvedById != null ? "approvedById=" + approvedById + ", " : "") +
            "}";
    }

}
