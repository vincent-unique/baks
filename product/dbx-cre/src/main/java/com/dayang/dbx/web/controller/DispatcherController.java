package com.dayang.dbx.web.controller;

import com.dayang.dbx.model.ConnectionManager;
import com.dayang.dbx.service.GeneralService;
import com.dayang.dbx.web.WebHelper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Vincent on 2017/8/28 0028.
 */

@Controller
public class DispatcherController {

    private static Logger logger = LoggerFactory.getLogger(DispatcherController.class);

    @RequestMapping(value = {"/","home"})
    public String homePage(){
        return "home";
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String showTables(HttpServletRequest request, Model model) {
        if(!ConnectionManager.online){
            logger.warn("None online ,redirect home page to Sign In");
            return "redirect:/home";
        }
        String pattern = ServletRequestUtils.getStringParameter(request,"pattern" ,"");
        List<String> tables;
        if(Strings.isNullOrEmpty(pattern)){
            tables = GeneralService.getDefaultTables(GeneralService.currentTableFile);
        }else {
            tables = GeneralService.getXTables(pattern);
        }
        model.addAttribute("tables", tables);
        return "tables";
    }

    @RequestMapping("/disconnect")
    public String disconnect() {
        WebHelper.releaseConnection();
        logger.info("Disconnecting and Offline.");
        return "redirect:/home";
    }


    //test page
    @RequestMapping("hello")
    public String test(){
        return "hello";
    }
}
