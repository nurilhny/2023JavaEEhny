package com.hny.mapper;


import com.hny.bean.Site;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SiteMapper {

    List<Site> getAllSites();


}
