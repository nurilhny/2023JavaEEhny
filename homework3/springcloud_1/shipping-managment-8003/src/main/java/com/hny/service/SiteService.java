package com.hny.service;
import com.hny.entity.Site;

import java.util.List;

public interface SiteService {
    List<Site> getAllSites();

    int addSite(Site site);

    int delSiteById(int siteId);


}
