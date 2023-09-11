package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.scheduled.FileScheduled;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * 开发者模式
 *
 * <p>
 * 废物本物
 * </p>
 *
 * @author sdadgz
 * @since 2023/9/9 19:30:33
 */
@RestController
@RequestMapping("/develop")
@RequiredArgsConstructor
public class DevelopController {

    private final FileScheduled fileScheduled;

    @DeleteMapping("/cleanFile")
    public Result cleanFile() {
        CompletableFuture.runAsync(fileScheduled::realDelete);
        return Result.success();
    }

    @DeleteMapping("/deleteFile")
    public Result deleteFile() {
        CompletableFuture.runAsync(fileScheduled::cleanFileNotInDatabase);
        return Result.success();
    }

}
