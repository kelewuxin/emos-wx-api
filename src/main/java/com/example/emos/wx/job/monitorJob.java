package com.example.emos.wx.job;

import cn.hutool.core.date.DateUtil;
import com.example.emos.wx.task.EmailTask;
import com.example.emos.wx.util.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author: renke
 * @description: 监控IP是否正常
 * @date: 2023/2/21 8:45
 * @version: 1.0
 */
@Component
@Slf4j
public class monitorJob {

    @Resource
    EmailTask emailTask;


    @Value("${emos.email.system}")
    private String sysEmail;

    /**
     * 每隔10分钟刷新一次查询是否下载
     */
    @Scheduled(cron = "0 0/60 * * * ?")
    public void execute() {
        String ip="61.184.33.38";
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
        }
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("来自")||line.contains("1 received")) {
                    log.info(ip + " 连接成功");
                }
                if (line.contains("请求超时")||line.contains("0 received")) {

                    log.info(ip + DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss") +" 连接失败");
                    SimpleMailMessage message=new SimpleMailMessage();
                    message.setTo(sysEmail);
                    message.setSubject("IP:" + ip + "网络异常警告！");
                    message.setText("IP:" + ip + "，" + DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss") + "网络访问异常请注意！");
                    emailTask.sendAsync(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
