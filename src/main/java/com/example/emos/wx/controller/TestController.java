package com.example.emos.wx.controller;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/5 15:21
 * @version: 1.0
 */

import com.baomidou.mybatisplus.extension.api.R;
import com.example.emos.wx.controller.form.TestSayHelloForm;
import com.example.emos.wx.db.pojo.SysConfig;
import com.example.emos.wx.db.service.SysConfigService;
import com.example.emos.wx.util.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/test")
@Api("测试Web接口")
@Slf4j
public class TestController {
    @Resource
    SysConfigService sysConfigService;

    @PostMapping("/sayHello")
    @ApiOperation("最简单的测试方法")
    public ResultMessage sayHello(@Valid @RequestBody TestSayHelloForm form){
        SysConfig sys=sysConfigService.sysconfig();
        log.info(String.valueOf(sys));
        return ResultMessage.ok().put("message","Hello,"+form.getName());
    }
    @PostMapping("/addUser")
    @ApiOperation("添加用户")
    @RequiresPermissions(value = {"ROOT", "USER:ADD"}, logical = Logical.OR)
    public ResultMessage addUser() {
        return ResultMessage.ok("用户添加成功");
    }


    /**
     * 测试网址是否可访问
     * @param request
     * @return 返回true可访问 返回false不可访问
     * @作者 <b><a class=b href="https://blog.csdn.net/lingdu_dou" color="red">⭕°</a></b>
     * @创建时间 2022-08-31 9:05     */
    @GetMapping("/ipConnection")
    @ApiOperation("网络测试方法")
    public ResultMessage ipConnection(HttpServletRequest request) {
        String ip="218.200.85.171";
        //获取操作系统类型
        String osName = "Windows";
        log.info("操作系统："+osName);
        String command = "";
        if(osName.contains("Linux")){
            command = "ping -c 1 -w 1 "+ip;
        }else if(osName.contains("Windows")){
            command = "ping -n 1 -w 1000 "+ip;
        }else {
            log.error("未知系统 执行ping命令失败");
            return ResultMessage.error("未知系统 执行ping命令失败");
        }
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("来自")||line.contains("1 received")) {
                    log.info(ip + " 连接成功");
                    return ResultMessage.ok("连接成功");
                }
                if (line.contains("请求超时")||line.contains("0 received")) {
                    log.info(ip + " 连接失败");
                    return ResultMessage.error("连接失败");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultMessage.error("连接失败2");
    }


}
