package com.ft.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the TimeEntry entity. This class is used in TimeEntryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /time-entries?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TimeEntryCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LocalDateFilter date;

    private DoubleFilter duration;

    private LongFilter timesheetId;

    private LongFilter jobCodeId;

    public TimeEntryCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public DoubleFilter getDuration() {
        return duration;
    }

    public void setDuration(DoubleFilter duration) {
        this.duration = duration;
    }

    public LongFilter getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(LongFilter timesheetId) {
        this.timesheetId = timesheetId;
    }

    public LongFilter getJobCodeId() {
        return jobCodeId;
    }

    public void setJobCodeId(LongFilter jobCodeId) {
        this.jobCodeId = jobCodeId;
    }

    @Override
    public String toString() {
        return "TimeEntryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (duration != null ? "duration=" + duration + ", " : "") +
                (timesheetId != null ? "timesheetId=" + timesheetId + ", " : "") +
                (jobCodeId != null ? "jobCodeId=" + jobCodeId + ", " : "") +
            "}";
    }

}
