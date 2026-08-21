package com.jp.business.service;

import com.jp.business.dto.LabourRequest;
import com.jp.business.dto.LabourResponse;
import com.jp.business.entity.Labour;
import com.jp.business.repository.LabourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LabourService {

    private final LabourRepository labourRepository;

    public LabourService(
            LabourRepository labourRepository
    ) {
        this.labourRepository = labourRepository;
    }

    @Transactional
    public Labour createLabour(
            LabourRequest request
    ) {

        if (labourRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException(
                    "Labour with this phone number already exists"
            );
        }

        Labour labour = new Labour();

        labour.setLabourNo(
                generateLabourNumber()
        );

        labour.setName(
                request.getName()
        );

        labour.setPhone(
                request.getPhone()
        );

        labour.setSkill(
                request.getSkill()
        );

        labour.setDailyWage(
                request.getDailyWage()
        );

        labour.setJoiningDate(
                request.getJoiningDate()
        );

        if (request.getActive() != null) {
            labour.setActive(
                    request.getActive()
            );
        }

        labour.setNotes(
                request.getNotes()
        );

        return labourRepository.save(labour);
    }

    private String generateLabourNumber() {

        long count =
                labourRepository.count() + 1;

        return String.format(
                "LAB-%04d",
                count
        );
    }

    @Transactional(readOnly = true)
    public List<Labour> getAllLabours() {

        return labourRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Labour> getActiveLabours() {

        return labourRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Labour> getInactiveLabours() {

        return labourRepository.findByActiveFalse();
    }

    @Transactional(readOnly = true)
    public Labour getLabourById(
            Long id
    ) {

        return labourRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Labour not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public Labour getLabourByNo(
            String labourNo
    ) {

        return labourRepository
                .findByLabourNo(labourNo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Labour not found: "
                                        + labourNo
                        )
                );
    }

    @Transactional
    public Labour updateLabour(
            Long id,
            LabourRequest request
    ) {

        Labour labour =
                getLabourById(id);

        if (!labour.getPhone().equals(request.getPhone())
                && labourRepository.existsByPhone(
                        request.getPhone()
                )) {

            throw new RuntimeException(
                    "Another labour with this phone number already exists"
            );
        }

        labour.setName(
                request.getName()
        );

        labour.setPhone(
                request.getPhone()
        );

        labour.setSkill(
                request.getSkill()
        );

        labour.setDailyWage(
                request.getDailyWage()
        );

        if (request.getJoiningDate() != null) {
            labour.setJoiningDate(
                    request.getJoiningDate()
            );
        }

        if (request.getActive() != null) {
            labour.setActive(
                    request.getActive()
            );
        }

        labour.setNotes(
                request.getNotes()
        );

        return labourRepository.save(labour);
    }

    @Transactional
    public void deactivateLabour(
            Long id
    ) {

        Labour labour =
                getLabourById(id);

        labour.setActive(false);

        labourRepository.save(labour);
    }

    @Transactional
    public void activateLabour(
            Long id
    ) {

        Labour labour =
                getLabourById(id);

        labour.setActive(true);

        labourRepository.save(labour);
    }

    public LabourResponse toResponse(
            Labour labour
    ) {

        LabourResponse response =
                new LabourResponse();

        response.setId(
                labour.getId()
        );

        response.setLabourNo(
                labour.getLabourNo()
        );

        response.setName(
                labour.getName()
        );

        response.setPhone(
                labour.getPhone()
        );

        response.setSkill(
                labour.getSkill()
        );

        response.setDailyWage(
                labour.getDailyWage()
        );

        BigDecimal hourlyWage =
                labour.getDailyWage()
                        .divide(
                                BigDecimal.valueOf(8),
                                2,
                                RoundingMode.HALF_UP
                        );

        response.setHourlyWage(
                hourlyWage
        );

        response.setJoiningDate(
                labour.getJoiningDate()
        );

        response.setActive(
                labour.isActive()
        );

        response.setNotes(
                labour.getNotes()
        );

        response.setCreatedAt(
                labour.getCreatedAt()
        );

        response.setUpdatedAt(
                labour.getUpdatedAt()
        );

        return response;
    }
}