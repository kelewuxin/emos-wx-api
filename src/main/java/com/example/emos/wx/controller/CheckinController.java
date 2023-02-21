package com.example.emos.wx.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.emos.wx.config.shiro.JwtUtil;
import com.example.emos.wx.controller.form.CheckinForm;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.CheckinService;
import com.example.emos.wx.util.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/17 20:42
 * @version: 1.0
 */
@RequestMapping("/checkin")
@RestController
@Api("签到模块Web接口")
@Slf4j
public class CheckinController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CheckinService checkinService;

    @Value("${emos.image-folder}")
    private String imageFolder;

    @GetMapping("/validCanCheckIn")
    @ApiOperation("查看用户今天是否可以签到")
    public ResultMessage validCanCheckIn(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        String result = checkinService.validCanCheckIn(userId, DateUtil.today());
        return ResultMessage.ok(result);
    }
    @PostMapping("/checkin")
    @ApiOperation("签到")
    public ResultMessage checkin(@Valid CheckinForm form, @RequestParam("photo") MultipartFile file, @RequestHeader("token") String token) {
        if (null == file) {
            return ResultMessage.error("没有上传文件");
        }
        int userId = jwtUtil.getUserId(token);
        String fileName = file.getOriginalFilename().toLowerCase();
        String path = imageFolder + "/" + fileName;
        if (!fileName.endsWith(".jpg")) {
            FileUtil.del(path);
            return ResultMessage.error("必须提交JPG格式图片");
        } else {
            try {
                file.transferTo(Paths.get(path));
                HashMap param = new HashMap();
                param.put("userId", userId);
                param.put("path", path);
                param.put("city", form.getCity());
                param.put("district", form.getDistrict());
                param.put("address", form.getAddress());
                param.put("country", form.getCountry());
                param.put("province", form.getProvince());
                checkinService.checkin(param);
                return ResultMessage.ok("签到成功");
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new EmosException("保存图片错误");
            } finally {
                FileUtil.del(path);
            }
        }
    }

    @PostMapping("/createFaceModel")
    @ApiOperation("创建人脸模型")
    public ResultMessage createFaceModel(@RequestParam("photo") MultipartFile file, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        if (file==null) {
            return ResultMessage.error("没有上传文件");
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        String path = imageFolder + "/" + fileName;
        if (!fileName.endsWith(".jpg")) {
            return ResultMessage.error("必须提交JPG格式图片");
        } else {
            try {
                file.transferTo(Paths.get(path));
                checkinService.createFaceModel(userId, path);
                return ResultMessage.ok("人脸建模成功");
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new EmosException("保存图片错误");
            } finally {
                FileUtil.del(path);
            }
        }
    }

}

