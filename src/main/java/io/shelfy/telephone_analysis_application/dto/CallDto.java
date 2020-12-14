package io.shelfy.telephone_analysis_application.dto;


public class CallDto {
    public long id;
    public long caller_id;
    public long called_party_id;
    public String timestamp;
    public long duration_in_seconds;

    public CallDto(){}

    public CallDto(long id, long caller_id, long called_party_id, String timestamp, long duration_in_seconds) {
        this.id = id;
        this.caller_id = caller_id;
        this.called_party_id = called_party_id;
        this.timestamp = timestamp;
        this.duration_in_seconds = duration_in_seconds;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                "caller_id=" + caller_id +
                ", called_party_id=" + called_party_id +
                ", timestamp='" + timestamp + '\'' +
                ", duration_in_seconds=" + duration_in_seconds;
    }
}
