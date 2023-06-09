package com.hny.mapper;


import com.hny.entity.Site;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SiteMapper {

    List<Site> getAllSites();

    int addSite(@Param("site") Site site);

    int delSiteById(int siteId);

}
