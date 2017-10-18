package com.test.controller.system;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.mapper.UserLoginMapper;
import com.test.controller.index.BaseController;
import com.test.entity.UserLoginFormMap;
import com.test.plugin.PageView;
import com.test.util.Common;

/**
 * @author test 2014-11-19
 * @version 3.0v
 * @Email: mmm333zzz520@163.com
 */
@Controller
@RequestMapping("/userlogin/")
public class UserLoginController extends BaseController {
    @Inject
    private UserLoginMapper userLoginMapper;

    @RequestMapping("listUI")
    public String listUI(Model model) throws Exception {
        return Common.BACKGROUND_PATH + "/system/userlogin/list";
    }

    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow,
                               String pageSize) throws Exception {
        UserLoginFormMap userLoginFormMap = getFormMap(UserLoginFormMap.class);
        userLoginFormMap = toFormMap(userLoginFormMap, pageNow, pageSize, userLoginFormMap.getStr("orderby"));
        pageView.setRecords(userLoginMapper.findByPage(userLoginFormMap));
        return pageView;
    }

}