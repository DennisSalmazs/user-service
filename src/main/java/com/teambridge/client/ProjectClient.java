package com.teambridge.client;

import com.teambridge.dto.ProjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "project-service", url = "localhost:8082")
public interface ProjectClient {

    @GetMapping("/api/v1/project/count/manager/{assignedManager}")
    ResponseEntity<ProjectResponse> getNonCompletedCountByAssignedManager(@PathVariable("assignedManager") String assignedManager);
}
