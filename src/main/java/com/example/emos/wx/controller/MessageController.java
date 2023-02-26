package com.example.emos.wx.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.emos.wx.config.shiro.JwtUtil;
import com.example.emos.wx.controller.form.SearchMessageByPageForm;
import com.example.emos.wx.service.MessageService;
import com.example.emos.wx.util.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/26 15:16
 * @version: 1.0
 */
@RestController
@RequestMapping("/message")
@Api("消息模块网络接口")
public class MessageController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MessageService messageService;

    @PostMapping("/searchMessageByPage")
    @ApiOperation("获取分页消息列表")
    public ResultMessage searchMessageByPage(@Valid @RequestBody SearchMessageByPageForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        int page = form.getPage();
        int length = form.getLength();
        long start = (page - 1) * length;
        List<HashMap> list = messageService.searchMessageByPage(userId, start, length);
        return ResultMessage.ok().put("result", list);
    }
}

