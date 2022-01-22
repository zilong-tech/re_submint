package com.demo.token.controller;

import com.demo.token.common.ConstantUtils;
import com.demo.token.util.ApiRepeatSubmit;
import com.demo.token.util.ApiToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {


    /**
     * 进入页面
     * @return
     */
    @GetMapping("/")
    @ApiToken
    public String index(){
        return "index";
    }


    /**
     * 测试重复提交接口
     * 将Token放入请求头中
     * @return
     */
    @RequestMapping("/test")
    @ApiRepeatSubmit(ConstantUtils.HEAD)
    public @ResponseBody String test() {
        return ("业务返回");
    }

} 