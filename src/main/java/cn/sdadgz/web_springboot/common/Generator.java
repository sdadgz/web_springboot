package cn.sdadgz.web_springboot.common;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class Generator {
    public static void main(String[] args) {
        // 我不想再改地址了
        String localPath = "D:/工具/编程/idea/java工作区/";
        String projectName = "web_springboot";
        String packagePath = "cn.sdadgz.web_springboot";
//        String localPath = "D:/CODE/";

        // 表名
        String tableName = "img";

        FastAutoGenerator.create(
                        "jdbc:mysql://sdadgz.cn:3306/website",
                        "root",
                        "123456")
                .globalConfig(builder -> {
                    builder.author("eula") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()//完成后不打开文件夹
                            // 指定输出目录
                            //直接右键复制项目根目录的绝对路径
                            .outputDir(localPath + projectName + "/src/main/java");
//                            .outputDir("D:/工具/编程/idea/java工作区/syes_springboot/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent(packagePath) // 设置父包名
                            // 设置mapperXml生成路径
                            //直接右键复制项目mapper文件夹的绝对路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml, localPath + projectName + "/src/main/resources/mapper"));
//                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:/工具/编程/idea/java工作区/syes_springboot/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName)//设置表名
                            .mapperBuilder()
                            .enableMapperAnnotation() // 开启mapper注解
                            .entityBuilder()    //entity前置，才能用lombok
                            .enableLombok()  //使用lombok
                            .controllerBuilder()
                            .enableRestStyle(); //restMapping注解
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
