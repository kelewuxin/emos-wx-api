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
import javax.validation.Valid;

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


}
