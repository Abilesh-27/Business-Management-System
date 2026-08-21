package com.jp.business.service;

import com.jp.business.dto.LabourWorkEntryRequest;
import com.jp.business.dto.LabourWorkEntryResponse;
import com.jp.business.entity.Labour;
import com.jp.business.entity.LabourWorkEntry;
import com.jp.business.entity.Site;
import com.jp.business.entity.WorkType;
import com.jp.business.repository.LabourRepository;
import com.jp.business.repository.LabourWorkEntryRepository;
import com.jp.business.repository.SiteRepository;
import com.jp.business.repository.WorkTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class LabourWorkEntryService {

    private static final LocalTime NORMAL_WORK_END =
            LocalTime.of(18, 0);

    private static final BigDecimal NORMAL_DAY_HOURS =
            BigDecimal.valueOf(8);

    private final LabourWorkEntryRepository workEntryRepository;
    private final LabourRepository labourRepository;
    private final SiteRepository siteRepository;
    private final WorkTypeRepository workTypeRepository;

    public LabourWorkEntryService(
            LabourWorkEntryRepository workEntryRepository,
            LabourRepository labourRepository,
            SiteRepository siteRepository,
            WorkTypeRepository workTypeRepository
    ) {
        this.workEntryRepository = workEntryRepository;
        this.labourRepository = labourRepository;
        this.siteRepository = siteRepository;
        this.workTypeRepository = workTypeRepository;
    }

    @Transactional
    public LabourWorkEntry createWorkEntry(
            LabourWorkEntryRequest request
    ) {

        Labour labour =
                labourRepository.findById(
                        request.getLabourId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Labour not found: "
                                        + request.getLabourId()
                        )
                );

        if (!labour.isActive()) {
            throw new RuntimeException(
                    "Cannot add work entry for inactive labour"
            );
        }

        Site site =
                siteRepository.findById(
                        request.getSiteId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Site not found: "
                                        + request.getSiteId()
                        )
                );

        if (!site.isActive()) {
            throw new RuntimeException(
                    "Cannot add work entry for inactive site"
            );
        }

        WorkType workType =
                workTypeRepository.findById(
                        request.getWorkTypeId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Work type not found: "
                                        + request.getWorkTypeId()
                        )
                );

        if (!workType.isActive()) {
            throw new RuntimeException(
                    "Cannot add work entry for inactive work type"
            );
        }

        validateTimes(
                request.getStartTime(),
                request.getEndTime()
        );

        int breakMinutes =
                request.getBreakMinutes() == null
                        ? 0
                        : request.getBreakMinutes();

        if (breakMinutes < 0) {
            throw new RuntimeException(
                    "Break minutes cannot be negative"
            );
        }

        BigDecimal totalHours =
                calculateTotalHours(
                        request.getStartTime(),
                        request.getEndTime(),
                        breakMinutes
                );

        if (totalHours.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Total working hours must be greater than zero"
            );
        }

        BigDecimal normalHours =
                calculateNormalHours(
                        request.getStartTime(),
                        request.getEndTime(),
                        breakMinutes
                );

        BigDecimal overtimeHours =
        totalHours.subtract(normalHours);

if (overtimeHours.compareTo(BigDecimal.ZERO) < 0) {
    overtimeHours = BigDecimal.ZERO;
}
        boolean sunday =
                request.getWorkDate()
                        .getDayOfWeek()
                        .name()
                        .equals("SUNDAY");

        boolean nightWork =
                isNightWork(
                        request.getStartTime()
                );

        BigDecimal hourlyWage =
                labour.getDailyWage()
                        .divide(
                                NORMAL_DAY_HOURS,
                                2,
                                RoundingMode.HALF_UP
                        );

        BigDecimal earningAmount =
                hourlyWage
                        .multiply(totalHours)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        LabourWorkEntry entry =
                new LabourWorkEntry();

        entry.setLabour(labour);
        entry.setSite(site);
        entry.setWorkType(workType);

        entry.setWorkDate(
                request.getWorkDate()
        );

        entry.setStartTime(
                request.getStartTime()
        );

        entry.setEndTime(
                request.getEndTime()
        );

        entry.setBreakMinutes(
                breakMinutes
        );

        entry.setTotalHours(
                totalHours
        );

        entry.setNormalHours(
                normalHours
        );

        entry.setOvertimeHours(
                overtimeHours
        );

        entry.setSunday(sunday);

        entry.setNightWork(nightWork);

        entry.setEarningAmount(
                earningAmount
        );

        entry.setNotes(
                request.getNotes()
        );

        return workEntryRepository.save(entry);
    }

    private void validateTimes(
            LocalTime startTime,
            LocalTime endTime
    ) {

        if (startTime == null || endTime == null) {
            throw new RuntimeException(
                    "Start time and end time are required"
            );
        }

        if (startTime.equals(endTime)) {
            throw new RuntimeException(
                    "Start time and end time cannot be the same"
            );
        }
    }

    private BigDecimal calculateTotalHours(
            LocalTime startTime,
            LocalTime endTime,
            int breakMinutes
    ) {

        long minutes;

        if (endTime.isAfter(startTime)) {

            minutes =
                    Duration.between(
                            startTime,
                            endTime
                    ).toMinutes();

        } else {

            /*
             * End time is on the next day.
             *
             * Example:
             * 10:00 PM → 02:00 AM
             */
            minutes =
                    Duration.between(
                            startTime,
                            endTime
                    ).toMinutes()
                            + (24 * 60);
        }

        minutes -= breakMinutes;

        if (minutes <= 0) {
            throw new RuntimeException(
                    "Break time cannot be greater than total work time"
            );
        }

        return BigDecimal.valueOf(minutes)
                .divide(
                        BigDecimal.valueOf(60),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private BigDecimal calculateNormalHours(
        LocalTime startTime,
        LocalTime endTime,
        int breakMinutes
) {

    /*
     * Normal working period ends at 6:00 PM.
     *
     * Maximum normal payable hours = 8.
     */

    if (!startTime.isBefore(NORMAL_WORK_END)) {
        return BigDecimal.ZERO;
    }

    LocalTime normalEnd;

    if (endTime.isAfter(startTime)
            && endTime.isBefore(NORMAL_WORK_END)) {

        normalEnd = endTime;

    } else {

        normalEnd = NORMAL_WORK_END;
    }

    long normalMinutes =
            Duration.between(
                    startTime,
                    normalEnd
            ).toMinutes();

    /*
     * Break is deducted from normal working time first.
     */
    normalMinutes -= Math.min(
            breakMinutes,
            (int) normalMinutes
    );

    if (normalMinutes <= 0) {
        return BigDecimal.ZERO;
    }

    /*
     * Normal salary is based on maximum 8 hours.
     */
    normalMinutes =
            Math.min(
                    normalMinutes,
                    8 * 60
            );

    return BigDecimal.valueOf(normalMinutes)
            .divide(
                    BigDecimal.valueOf(60),
                    2,
                    RoundingMode.HALF_UP
            );
}

    private boolean isNightWork(
            LocalTime startTime
    ) {

        /*
         * Work starting at or after 6 PM
         * is considered night work.
         */
        return !startTime.isBefore(
                NORMAL_WORK_END
        );
    }

    @Transactional(readOnly = true)
    public List<LabourWorkEntry>
    getAllWorkEntries() {

        return workEntryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public LabourWorkEntry
    getWorkEntryById(Long id) {

        return workEntryRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Work entry not found: "
                                        + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<LabourWorkEntry>
    getLabourWorkHistory(Long labourId) {

        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException(
                    "Labour not found: " + labourId
            );
        }

        return workEntryRepository
                .findByLabourIdOrderByWorkDateDesc(
                        labourId
                );
    }

    @Transactional(readOnly = true)
    public List<LabourWorkEntry>
    getWorkEntriesByDate(
            LocalDate date
    ) {

        return workEntryRepository
                .findByWorkDateOrderByLabourId(
                        date
                );
    }

    @Transactional(readOnly = true)
    public List<LabourWorkEntry>
    getLabourWorkBetweenDates(
            Long labourId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException(
                    "Labour not found: " + labourId
            );
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        return workEntryRepository
                .findByLabourIdAndWorkDateBetweenOrderByWorkDateAsc(
                        labourId,
                        startDate,
                        endDate
                );
    }

    @Transactional(readOnly = true)
    public List<LabourWorkEntry>
    getSiteWorkBetweenDates(
            Long siteId,
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (!siteRepository.existsById(siteId)) {
            throw new RuntimeException(
                    "Site not found: " + siteId
            );
        }

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        return workEntryRepository
                .findBySiteIdAndWorkDateBetweenOrderByWorkDateAsc(
                        siteId,
                        startDate,
                        endDate
                );
    }

    public LabourWorkEntryResponse toResponse(
            LabourWorkEntry entry
    ) {

        LabourWorkEntryResponse response =
                new LabourWorkEntryResponse();

        Labour labour =
                entry.getLabour();

        Site site =
                entry.getSite();

        WorkType workType =
                entry.getWorkType();

        response.setId(entry.getId());

        response.setLabourId(
                labour.getId()
        );

        response.setLabourNo(
                labour.getLabourNo()
        );

        response.setLabourName(
                labour.getName()
        );

        response.setSiteId(
                site.getId()
        );

        response.setSiteName(
                site.getSiteName()
        );

        response.setWorkTypeId(
                workType.getId()
        );

        response.setWorkTypeName(
                workType.getName()
        );

        response.setWorkDate(
                entry.getWorkDate()
        );

        response.setStartTime(
                entry.getStartTime()
        );

        response.setEndTime(
                entry.getEndTime()
        );

        response.setBreakMinutes(
                entry.getBreakMinutes()
        );

        response.setTotalHours(
                entry.getTotalHours()
        );

        response.setNormalHours(
                entry.getNormalHours()
        );

        response.setOvertimeHours(
                entry.getOvertimeHours()
        );

        response.setSunday(
                entry.isSunday()
        );

        response.setNightWork(
                entry.isNightWork()
        );

        response.setDailyWage(
                labour.getDailyWage()
        );

        BigDecimal hourlyWage =
                labour.getDailyWage()
                        .divide(
                                NORMAL_DAY_HOURS,
                                2,
                                RoundingMode.HALF_UP
                        );

        response.setHourlyWage(
                hourlyWage
        );

        response.setEarningAmount(
                entry.getEarningAmount()
        );

        response.setNotes(
                entry.getNotes()
        );

        response.setCreatedAt(
                entry.getCreatedAt()
        );

        response.setUpdatedAt(
                entry.getUpdatedAt()
        );

        return response;
    }
}