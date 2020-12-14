package io.shelfy.telephone_analysis_application.dto;

public class CallDtoWithDuration {
    public long id;
    public long caller_id;
    public long called_party_id;
    public String timestamp;
    public long duration_in_seconds;
    public long total_duration;

    public CallDtoWithDuration(){}

    public CallDtoWithDuration(long id, long caller_id, long called_party_id, String timestamp, long duration_in_seconds, long total_duration) {
        this.id = id;
        this.caller_id = caller_id;
        this.called_party_id = called_party_id;
        this.timestamp = timestamp;
        this.duration_in_seconds = duration_in_seconds;
        this.total_duration = total_duration;
    }


}
