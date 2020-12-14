package io.shelfy.telephone_analysis_application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.shelfy.telephone_analysis_application.dto.*;
import io.shelfy.telephone_analysis_application.service.TelephoneCallAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = TelephoneCallAnalysisController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class TelephoneCallAnalysisController {
    static final String REST_URL = "/callers";

    private TelephoneCallAnalysisService service;

    @Autowired
    public void setInjection(TelephoneCallAnalysisService service) {
        this.service = service;
    }


    @GetMapping
    public List<CallerDto> getAllCallers() {
        return service.getAllCallers();
    }

    @GetMapping(value = "/pageable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CallersPageableDto> getAllCallersPageable(
            @RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer itemsOnPage,
            @RequestParam(defaultValue = "id") String sortBy) {
        HttpHeaders responseHeaders = createHeaders();
        CallersPageableDto list = service.getAllCallersPageable(currentPage, itemsOnPage, sortBy);
        return new ResponseEntity<>(list, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/namesOrEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CallerDto>> getAllCallersByNamesOrEmail(
            @RequestParam String searchString, HttpServletResponse response) throws IOException {
        HttpHeaders responseHeaders = createHeaders();
        List<CallerDto> list = service.getAllCallersByNamesOrEmail(searchString);
        if (list.size() == 0) {
            return new ResponseEntity(new ObjectMapper().writeValueAsString("Nothing was found"), responseHeaders, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/calls/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CallerDto>> getAllCallersContacts(
            @RequestParam long id, HttpServletResponse response) throws IOException {
        HttpHeaders responseHeaders = createHeaders();
        List<CallerDto> list = service.getAllCallersContacts(id);
        if (list.size() == 0) {
            return new ResponseEntity(new ObjectMapper().writeValueAsString("Nothing was found"), responseHeaders, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/calls", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CallsStatisticsDto> getAllCalls(
            @RequestParam long callerId, @RequestParam long contactId, HttpServletResponse response) throws IOException {
        HttpHeaders responseHeaders = createHeaders();
        CallsStatisticsDto list = service.getAllCalls(callerId, contactId);
        return new ResponseEntity<>(list, responseHeaders, HttpStatus.OK);
    }

    @PutMapping(value = "/calls/edit/{callId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCall(@PathVariable long callId, @RequestBody CallDto callDto, HttpServletResponse response) throws IOException {
        CallDtoWithDuration returnCallDto = service.update(callId, callDto);
        if (returnCallDto != null) {
            return new ResponseEntity<>(returnCallDto, null, HttpStatus.OK);
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping(value = "/calls/delete/{callId}")
    public  ResponseEntity<?>  deleteCall(@PathVariable long callId) {
        long  totalDuration = service.delete(callId);
        return new ResponseEntity<Long>(totalDuration, null, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CallDtoWithDuration> addCall(@RequestBody CallDto callDto) {
        CallDtoWithDuration call = service.addCall(callDto);
        if (call != null) {
            return new ResponseEntity<>(call, null, HttpStatus.OK);
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");
        return responseHeaders;
    }
}
