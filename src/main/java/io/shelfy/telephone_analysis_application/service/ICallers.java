package io.shelfy.telephone_analysis_application.service;

import io.shelfy.telephone_analysis_application.dto.CallerDto;
import io.shelfy.telephone_analysis_application.dto.CallersPageableDto;

import java.util.List;

public interface ICallers {
   List<CallerDto> getAllCallers();
   CallersPageableDto getAllCallersPageable(int pageNo, int pageSize, String sortBy);
   List<CallerDto> getAllCallersByNamesOrEmail(String searchString);
}
