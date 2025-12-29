package com.aashray.hiremate.application.mapper;


import com.aashray.hiremate.application.dto.ApplicationHistoryResponse;
import com.aashray.hiremate.application.entity.ApplicationStatusHistory;
import org.springframework.stereotype.Component;

@Component
public class ApplicationHistoryMapper {
    public ApplicationHistoryResponse createResponse(ApplicationStatusHistory history){
        return ApplicationHistoryResponse.builder()
                .from(history.getFromStatus())
                .to(history.getToStatus())
                .changedAt(history.getChangedAt())
                .build();
    }
}
