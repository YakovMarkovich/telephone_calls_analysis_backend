package io.shelfy.telephone_analysis_application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.shelfy.telephone_analysis_application.dto.CallDto;
import io.shelfy.telephone_analysis_application.dto.CallDtoWithDuration;
import io.shelfy.telephone_analysis_application.dto.CallerDto;
import io.shelfy.telephone_analysis_application.dto.CallsStatisticsDto;

import java.util.List;

public interface ICalls {
    List<CallerDto> getAllCallersContacts(long id);
    CallsStatisticsDto getAllCalls(long callerId, long contactId) throws JsonProcessingException;
    CallDtoWithDuration update(long id, CallDto callDto);
    long delete(long callId);
    CallDtoWithDuration addCall(CallDto callDto);
}
