package com.jp.business.controller;

import com.jp.business.dto.WorkTypeRequest;
import com.jp.business.dto.WorkTypeResponse;
import com.jp.business.entity.WorkType;
import com.jp.business.service.WorkTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/work-types")
@CrossOrigin(origins = "http://localhost:5173")
public class WorkTypeController {

    private final WorkTypeService workTypeService;

    public WorkTypeController(
            WorkTypeService workTypeService
    ) {
        this.workTypeService = workTypeService;
    }

    @PostMapping
    public ResponseEntity<WorkTypeResponse> createWorkType(
            @Valid @RequestBody WorkTypeRequest request
    ) {

        WorkType workType =
                workTypeService.createWorkType(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        workTypeService.toResponse(workType)
                );
    }

    @GetMapping
    public ResponseEntity<List<WorkTypeResponse>>
    getAllWorkTypes() {

        List<WorkTypeResponse> response =
                workTypeService
                        .getAllWorkTypes()
                        .stream()
                        .map(workTypeService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<WorkTypeResponse>>
    getActiveWorkTypes() {

        List<WorkTypeResponse> response =
                workTypeService
                        .getActiveWorkTypes()
                        .stream()
                        .map(workTypeService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<WorkTypeResponse>>
    getInactiveWorkTypes() {

        List<WorkTypeResponse> response =
                workTypeService
                        .getInactiveWorkTypes()
                        .stream()
                        .map(workTypeService::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkTypeResponse>
    getWorkType(
            @PathVariable Long id
    ) {

        WorkType workType =
                workTypeService.getWorkTypeById(id);

        return ResponseEntity.ok(
                workTypeService.toResponse(workType)
        );
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<WorkTypeResponse>
    getWorkTypeByName(
            @PathVariable String name
    ) {

        WorkType workType =
                workTypeService.getWorkTypeByName(name);

        return ResponseEntity.ok(
                workTypeService.toResponse(workType)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkTypeResponse>
    updateWorkType(
            @PathVariable Long id,
            @Valid @RequestBody WorkTypeRequest request
    ) {

        WorkType workType =
                workTypeService.updateWorkType(
                        id,
                        request
                );

        return ResponseEntity.ok(
                workTypeService.toResponse(workType)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void>
    deactivateWorkType(
            @PathVariable Long id
    ) {

        workTypeService.deactivateWorkType(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void>
    activateWorkType(
            @PathVariable Long id
    ) {

        workTypeService.activateWorkType(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}