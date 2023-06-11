package com.hny.service.impl;


import com.hny.entity.Site;
import com.hny.mapper.SiteMapper;
import com.hny.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    SiteMapper siteMapper;

    public List<Site> getAllSites(){
        return siteMapper.getAllSites();
    }

    public int addSite(Site site) {
        return siteMapper.addSite(site);
    }

    public int delSiteById(int siteId) {
        return siteMapper.delSiteById(siteId);
    }


}
