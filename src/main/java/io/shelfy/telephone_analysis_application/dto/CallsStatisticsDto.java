package io.shelfy.telephone_analysis_application.dto;


public class CallsStatisticsDto {

    public CallDto[] contactedCalls;
    public long duration;

    public CallsStatisticsDto() {
    }

    public CallsStatisticsDto(CallDto[] contactedCalls,  long duration) {
        this.contactedCalls = contactedCalls;
        this.duration = duration;
    }
}
	
	
	
	
