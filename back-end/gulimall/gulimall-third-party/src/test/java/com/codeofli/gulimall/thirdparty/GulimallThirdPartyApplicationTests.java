package com.codeofli.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/**
 * 1、引入oss-starter
 * 2、配置key，endpoint相关信息即可
 * 3、使用OSSClient 进行相关操作
 */
@SpringBootTest
class GulimallThirdPartyApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private OSSClient ossClient;

	@Test
	public void testUploadByAli() throws FileNotFoundException {
		InputStream inputStream = new FileInputStream("D:\\Codes\\gulimail-code\\code-resources\\分布式基础-资源\\pics\\xiaomi.png");
		ossClient.putObject("gulimall-hello-sso", "upload3.png", inputStream);
		System.out.println("上传成功");
	}


	@Test
	public void testUpload() throws FileNotFoundException {
		// Endpoint以杭州为例，其它Region请按实际情况填写。
		String endpoint = "oss-cn-nanjing.aliyuncs.com";
		// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
		String accessKeyId = "";
		String accessKeySecret = "";

		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

		// 上传文件流。
		InputStream inputStream = new FileInputStream("D:\\Codes\\gulimail-code\\code-resources\\分布式基础-资源\\pics\\huawei.png");
		ossClient.putObject("gulimall-hello-sso", "upload1.png", inputStream);

		// 关闭OSSClient。
		ossClient.shutdown();
		System.out.println("上传成功");
	}
}
