package com.jp.business.service;

import com.jp.business.dto.SiteRequest;
import com.jp.business.dto.SiteResponse;
import com.jp.business.entity.Site;
import com.jp.business.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteService(
            SiteRepository siteRepository
    ) {
        this.siteRepository = siteRepository;
    }

    @Transactional
    public Site createSite(
            SiteRequest request
    ) {

        if (siteRepository.existsBySiteName(
                request.getSiteName()
        )) {
            throw new RuntimeException(
                    "Site with this name already exists"
            );
        }

        Site site = new Site();

        site.setSiteName(
                request.getSiteName()
        );

        site.setAddress(
                request.getAddress()
        );

        site.setArea(
                request.getArea()
        );

        site.setCity(
                request.getCity()
        );

        site.setDistrict(
                request.getDistrict()
        );

        site.setState(
                request.getState()
        );

        site.setPincode(
                request.getPincode()
        );

        if (request.getActive() != null) {
            site.setActive(
                    request.getActive()
            );
        }

        site.setNotes(
                request.getNotes()
        );

        return siteRepository.save(site);
    }

    @Transactional(readOnly = true)
    public List<Site> getAllSites() {

        return siteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Site> getActiveSites() {

        return siteRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Site> getInactiveSites() {

        return siteRepository.findByActiveFalse();
    }

    @Transactional(readOnly = true)
    public Site getSiteById(
            Long id
    ) {

        return siteRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Site not found: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public Site getSiteByName(
            String siteName
    ) {

        return siteRepository
                .findBySiteName(siteName)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Site not found: "
                                        + siteName
                        )
                );
    }

    @Transactional
    public Site updateSite(
            Long id,
            SiteRequest request
    ) {

        Site site =
                getSiteById(id);

        if (!site.getSiteName().equals(
                request.getSiteName()
        )
                && siteRepository.existsBySiteName(
                request.getSiteName()
        )) {

            throw new RuntimeException(
                    "Another site with this name already exists"
            );
        }

        site.setSiteName(
                request.getSiteName()
        );

        site.setAddress(
                request.getAddress()
        );

        site.setArea(
                request.getArea()
        );

        site.setCity(
                request.getCity()
        );

        site.setDistrict(
                request.getDistrict()
        );

        site.setState(
                request.getState()
        );

        site.setPincode(
                request.getPincode()
        );

        if (request.getActive() != null) {
            site.setActive(
                    request.getActive()
            );
        }

        site.setNotes(
                request.getNotes()
        );

        return siteRepository.save(site);
    }

    @Transactional
    public void deactivateSite(
            Long id
    ) {

        Site site =
                getSiteById(id);

        site.setActive(false);

        siteRepository.save(site);
    }

    @Transactional
    public void activateSite(
            Long id
    ) {

        Site site =
                getSiteById(id);

        site.setActive(true);

        siteRepository.save(site);
    }

    public SiteResponse toResponse(
            Site site
    ) {

        SiteResponse response =
                new SiteResponse();

        response.setId(
                site.getId()
        );

        response.setSiteName(
                site.getSiteName()
        );

        response.setAddress(
                site.getAddress()
        );

        response.setArea(
                site.getArea()
        );

        response.setCity(
                site.getCity()
        );

        response.setDistrict(
                site.getDistrict()
        );

        response.setState(
                site.getState()
        );

        response.setPincode(
                site.getPincode()
        );

        response.setActive(
                site.isActive()
        );

        response.setNotes(
                site.getNotes()
        );

        response.setCreatedAt(
                site.getCreatedAt()
        );

        response.setUpdatedAt(
                site.getUpdatedAt()
        );

        return response;
    }
}