package io.shelfy.telephone_analysis_application.repository;

import io.shelfy.telephone_analysis_application.entities.Call;
import io.shelfy.telephone_analysis_application.entities.Caller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface CallRepository extends JpaRepository<Call, Long> {

    @Query("SELECT c FROM Caller c WHERE c.id IN (SELECT cl.calledParty FROM Call cl WHERE cl.caller.id=:id)")
    List<Caller> findAllContacts(@Param("id") long id);


    List<Call> findByCallerIdAndCalledPartyIdOrderByTimestamp(long caller_id, long called_party_id);
}

