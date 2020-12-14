package io.shelfy.telephone_analysis_application.repository;

import io.shelfy.telephone_analysis_application.dto.CallerDto;
import io.shelfy.telephone_analysis_application.entities.Caller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CallerRepository extends JpaRepository<Caller, Long> {

    @Query("SELECT c FROM Caller c WHERE lower(c.firstName) LIKE lower(concat('%', :string,'%'))  OR lower(c.lastName) LIKE lower(concat('%', :string,'%')) OR lower(c.email) LIKE lower(concat('%', :string,'%')) ")
    List<Caller> findAllByNamesOrEmail(@Param("string") String searchString);

    Caller[] findAllByIdBetween(long lastId, long totalElements);

}

