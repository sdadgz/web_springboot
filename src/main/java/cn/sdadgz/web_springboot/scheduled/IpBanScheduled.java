package cn.sdadgz.web_springboot.scheduled;

import cn.sdadgz.web_springboot.entity.IpBan;
import cn.sdadgz.web_springboot.service.IIpBanService;
import cn.sdadgz.web_springboot.utils.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

@EnableScheduling
@Configuration
@Slf4j
public class IpBanScheduled {

    @Resource
    private IIpBanService ipBanService;

    // 每分钟原谅多少人
    public static final int FORGIVE_PER_WEEK = 1;

    @Scheduled(cron = "30 30 6 ? * 5")
    void delete() {
        List<IpBan> gc = ipBanService.getGC();
        boolean b = ipBanService.removeBatchByIds(gc);
        log.info("ipBan获取到{}条垃圾，删除{}", gc.size(), GeneralUtil.tf(b));
    }

    @Scheduled(cron = "22 * * * * ?")
    void forgive() {
        List<IpBan> ipBanPage = ipBanService.getIpBanPage(0, FORGIVE_PER_WEEK);
        boolean b = false;
        if (ipBanPage.size() > 0) {
            b = ipBanService.removeBatchByIds(ipBanPage);
        }
        log.info("原谅了{}人次搞破坏的，删除{}", ipBanPage.size(), GeneralUtil.tf(b));
    }

}
