package io.shelfy.telephone_analysis_application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.shelfy.telephone_analysis_application.dto.*;
import io.shelfy.telephone_analysis_application.entities.Call;
import io.shelfy.telephone_analysis_application.entities.Caller;
import io.shelfy.telephone_analysis_application.repository.CallRepository;
import io.shelfy.telephone_analysis_application.repository.CallerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class TelephoneCallAnalysisService implements ICalls, ICallers{
    private CallerRepository callers;
    private CallRepository calls;

    @Autowired
    public void setInjection(CallerRepository callers, CallRepository calls) {
        this.callers = callers;
        this.calls = calls;
    }


    public List<CallerDto> getAllCallers() {
        List<CallerDto> list = new ArrayList<>();
        List<Caller> callersList = callers.findAll();
        for (Caller c : callersList) {
            list.add(new CallerDto(c.getId(), c.getEmail(), c.getFirstName(), c.getLastName(), c.getGender(), c.getImage()));
        }
        return list;
    }


    public CallersPageableDto getAllCallersPageable(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Caller> pagedResult = callers.findAll(paging);
        if (pagedResult.getContent().size() > 0) {
            int size = pagedResult.getContent().size();
            Caller[] callersArray = pagedResult.getContent().toArray(new Caller[size]);
            CallerDto[] callerDtoArray = convertArrayCallersToDto(callersArray);
            CallersPageableDto pageable = new CallersPageableDto(pagedResult.getTotalElements(), pagedResult.getNumberOfElements(), pagedResult.getNumber(), callerDtoArray, pagedResult.getTotalElements() > callerDtoArray[callerDtoArray.length - 1].id);
            return pageable;
        } else {
            paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
            pagedResult = callers.findAll(paging);
            long lastId = pagedResult.getContent().get(0).getId();
            long totalElements = pagedResult.getTotalElements();
            Caller[] callersArray = callers.findAllByIdBetween(lastId, totalElements);
            CallerDto[] callerDtoArray = convertArrayCallersToDto(callersArray);
            CallersPageableDto pageable = new CallersPageableDto(pagedResult.getTotalElements(), pagedResult.getNumberOfElements(), pagedResult.getNumber(), callerDtoArray, false);
            return pageable;
        }
    }

    private CallerDto[] convertArrayCallersToDto(Caller[] callersArray) {
        CallerDto[] callerDtoArray = new CallerDto[callersArray.length];
        for (int i = 0; i < callerDtoArray.length; i++) {
            callerDtoArray[i] = new CallerDto(callersArray[i].getId(), callersArray[i].getEmail(),
                    callersArray[i].getFirstName(), callersArray[i].getLastName(), callersArray[i].getGender(), callersArray[i].getImage());
        }
        return callerDtoArray;
    }

    private CallDto[] convertArrayCallsToDto(Call[] callsArray) {
        CallDto[] callDtoArray = new CallDto[callsArray.length];
        for (int i = 0; i < callDtoArray.length; i++) {
            callDtoArray[i] = new CallDto(callsArray[i].getId(), callsArray[i].getCaller().getId(), callsArray[i].getCalledParty().getId(),
                    callsArray[i].getTimestamp().toString(), callsArray[i].getDurationInSeconds());
        }
        return callDtoArray;
    }

    public List<CallerDto> getAllCallersByNamesOrEmail(String searchString) {
        List<CallerDto> listDto = new ArrayList<>();
        List<Caller> list = callers.findAllByNamesOrEmail(searchString);
        for (Caller c : list) {
            listDto.add(new CallerDto(c.getId(), c.getEmail(), c.getFirstName(), c.getLastName(), c.getGender(), c.getImage()));
        }
        return listDto;
    }

    public List<CallerDto> getAllCallersContacts(long id) {
        List<CallerDto> listDto = new ArrayList<>();
        List<Caller> list = calls.findAllContacts(id);
        for (Caller c : list) {
            listDto.add(new CallerDto(c.getId(), c.getEmail(), c.getFirstName(), c.getLastName(), c.getGender(), c.getImage()));
        }
        return listDto;
    }

    public CallsStatisticsDto getAllCalls(long callerId, long contactId) throws JsonProcessingException {
        List<Call> list = calls.findByCallerIdAndCalledPartyIdOrderByTimestamp(callerId, contactId);
        CallDto[] arrayDto = convertArrayCallsToDto(list.toArray(new Call[list.size()]));
        long duration = list.stream().map(c -> c.getDurationInSeconds()).reduce(0L, (a, b) -> a + b);
        return new CallsStatisticsDto(arrayDto, duration);
    }

    public CallDtoWithDuration update(long id, CallDto callDto) {
        if (calls.existsById(id)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Call call = calls.findById(id).orElse(null);
            call.setDurationInSeconds(callDto.duration_in_seconds);
            call.setTimestamp(LocalDateTime.parse(callDto.timestamp, dtf));
            Call callSaved = calls.save(call);
            List<Call> list = calls.findByCallerIdAndCalledPartyIdOrderByTimestamp(callDto.caller_id, callDto.called_party_id);
            long totalDuration = calculateTotalDuration(list);
            return new CallDtoWithDuration(callSaved.getId(), callSaved.getCaller().getId(), callSaved.getCalledParty().getId(),
                    callSaved.getTimestamp().toString(), callSaved.getDurationInSeconds(), totalDuration);
        } else return null;

    }

    public long delete(long callId) {
        long totalDuration = 0;
        if (calls.existsById(callId)) {
            Call call = calls.findById(callId).orElse(null);
            if (call != null) {
                calls.deleteById(callId);
            }
            List<Call> callList = calls.findByCallerIdAndCalledPartyIdOrderByTimestamp(call.getCaller().getId(), call.getCalledParty().getId());
            totalDuration = calculateTotalDuration(callList);
        }
        return totalDuration;
    }

    public CallDtoWithDuration addCall(CallDto callDto) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Call call = new Call(LocalDateTime.parse(callDto.timestamp, dtf), callDto.duration_in_seconds,
                callers.findById(callDto.caller_id).orElse(null),
                callers.findById(callDto.called_party_id).orElse(null));
        Call callSaved = calls.save(call);
        List<Call> list = calls.findByCallerIdAndCalledPartyIdOrderByTimestamp(callDto.caller_id, callDto.called_party_id);
        long totalDuration = calculateTotalDuration(list);
        return new CallDtoWithDuration(callSaved.getId(), callSaved.getCaller().getId(), callSaved.getCalledParty().getId(),
                callSaved.getTimestamp().toString(), callSaved.getDurationInSeconds(), totalDuration);
    }
    private long calculateTotalDuration (List<Call> list){
        return list.stream().map(c -> c.getDurationInSeconds()).reduce(0L, (a, b) -> a + b);
    }
}


