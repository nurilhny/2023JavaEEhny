package com.hny.controller;

import com.hny.bean.Site;
import com.hny.service.SiteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SiteController {
    @Autowired
    SiteService siteService;

    // 获得所有商品
    @GetMapping("/sites")
    public List<Site> getAllSites(){
        return siteService.getAllSites();
    }

    // 添加商品
    @PostMapping("/sites")
    public boolean addSite(Site site){

        int flag = siteService.addSite(site);
        if(flag==1){
            System.out.println("添加成功！");
            return true;
        }else {
            System.out.println("添加失败！");
            return false;
        }
    }

    // 通过id删除商品
    @DeleteMapping("/sites/{siteId}")
    public boolean delSiteById(@PathVariable("siteId") int siteId){
        int flag = siteService.delSiteById(siteId);
        if(flag == 1){
            System.out.println("删除成功！");
            return true;
        }else {
            System.out.println("删除失败！");
            return false;
        }
    }
}
