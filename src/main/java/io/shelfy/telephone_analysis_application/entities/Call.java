package io.shelfy.telephone_analysis_application.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "calls", indexes = {@Index(columnList = "caller_id")})
public class Call {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private LocalDateTime timestamp;
    @Column(name = "duration_in_seconds")
    private long durationInSeconds;
    @ManyToOne
    @JoinColumn(name = "caller_id")
    private Caller caller;
    @ManyToOne
    @JoinColumn(name = "called_party_id")
    private Caller calledParty;

    public Call() {
    }

    public Call(LocalDateTime timestamp, long durationInSeconds, Caller caller, Caller calledParty) {
        this.timestamp = timestamp;
        this.durationInSeconds = durationInSeconds;
        this.caller = caller;
        this.calledParty = calledParty;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public Caller getCaller() {
        return caller;
    }

    public Caller getCalledParty() {
        return calledParty;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDurationInSeconds(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public void setCaller(Caller caller) {
        this.caller = caller;
    }

    public void setCalledParty(Caller calledParty) {
        this.calledParty = calledParty;
    }
}