package com.jp.business.service;

import com.jp.business.dto.WorkTypeRequest;
import com.jp.business.dto.WorkTypeResponse;
import com.jp.business.entity.WorkType;
import com.jp.business.repository.WorkTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkTypeService {

    private final WorkTypeRepository workTypeRepository;

    public WorkTypeService(
            WorkTypeRepository workTypeRepository
    ) {
        this.workTypeRepository = workTypeRepository;
    }

    @Transactional
    public WorkType createWorkType(
            WorkTypeRequest request
    ) {

        String name = request.getName().trim();

        if (workTypeRepository.existsByName(name)) {
            throw new RuntimeException(
                    "Work type already exists: " + name
            );
        }

        WorkType workType = new WorkType();

        workType.setName(name);

        workType.setDescription(
                request.getDescription()
        );

        if (request.getActive() != null) {
            workType.setActive(
                    request.getActive()
            );
        }

        return workTypeRepository.save(workType);
    }

    @Transactional(readOnly = true)
    public List<WorkType> getAllWorkTypes() {

        return workTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<WorkType> getActiveWorkTypes() {

        return workTypeRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<WorkType> getInactiveWorkTypes() {

        return workTypeRepository.findByActiveFalse();
    }

    @Transactional(readOnly = true)
    public WorkType getWorkTypeById(
            Long id
    ) {

        return workTypeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Work type not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public WorkType getWorkTypeByName(
            String name
    ) {

        return workTypeRepository
                .findByName(name)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Work type not found: " + name
                        )
                );
    }

    @Transactional
    public WorkType updateWorkType(
            Long id,
            WorkTypeRequest request
    ) {

        WorkType workType =
                getWorkTypeById(id);

        String newName =
                request.getName().trim();

        if (!workType.getName().equals(newName)
                && workTypeRepository.existsByName(
                newName
        )) {

            throw new RuntimeException(
                    "Another work type with this name already exists"
            );
        }

        workType.setName(newName);

        workType.setDescription(
                request.getDescription()
        );

        if (request.getActive() != null) {
            workType.setActive(
                    request.getActive()
            );
        }

        return workTypeRepository.save(workType);
    }

    @Transactional
    public void deactivateWorkType(
            Long id
    ) {

        WorkType workType =
                getWorkTypeById(id);

        workType.setActive(false);

        workTypeRepository.save(workType);
    }

    @Transactional
    public void activateWorkType(
            Long id
    ) {

        WorkType workType =
                getWorkTypeById(id);

        workType.setActive(true);

        workTypeRepository.save(workType);
    }

    public WorkTypeResponse toResponse(
            WorkType workType
    ) {

        WorkTypeResponse response =
                new WorkTypeResponse();

        response.setId(
                workType.getId()
        );

        response.setName(
                workType.getName()
        );

        response.setDescription(
                workType.getDescription()
        );

        response.setActive(
                workType.isActive()
        );

        response.setCreatedAt(
                workType.getCreatedAt()
        );

        response.setUpdatedAt(
                workType.getUpdatedAt()
        );

        return response;
    }
}