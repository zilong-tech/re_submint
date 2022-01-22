package com.demo.aop;

import com.demo.token.common.ConstantUtils;
import com.demo.token.util.ApiRepeatSubmit;
import com.demo.token.util.ApiToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
public class ControllerDemo {


    /**
     * 进入页面
     * @return
     */
    @GetMapping("/aop")
    public String index(){
        return "order";
    }


    /**
     * 测试重复提交接口
     * 将Token放入请求头中
     * @return
     */
    @RequestMapping("/testAop")
    @Resubmit(delaySeconds = 30)
    public @ResponseBody String test() {
        try {
            // 模拟业务处理时间
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ("业务返回");
    }

} 