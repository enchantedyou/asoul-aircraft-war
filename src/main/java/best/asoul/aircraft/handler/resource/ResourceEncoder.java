package best.asoul.aircraft.handler.resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.entity.ResourceType;
import best.asoul.aircraft.exception.AsoulException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 资源加密处理器
 * @Author Enchantedyou
 * @Date 2021年12月23日-11:07
 */
@Slf4j
public class ResourceEncoder {

	private ResourceEncoder() {
	}

	/**
	 * @Description 将指定目录下的资源文件合成一个加密的文件
	 * @Author Enchantedyou
	 * @Date 2021/12/23-13:24
	 * @param dirPath
	 * @param resourceType
	 */
	public static void encode(String dirPath, ResourceType resourceType) {
		final File file = new File(dirPath);
		final File[] resourceFiles = file.listFiles();
		if (!file.exists() || !file.isDirectory() || null == resourceFiles) {
			log.warn("目录[{}]不存在", dirPath);
			return;
		}

		// 单个文件加密
		StringBuilder builder = new StringBuilder();
		for (File resourceFile : resourceFiles) {
			if (resourceFile.isDirectory()) {
				encode(resourceFile.getPath(), resourceType);
			} else if (!resourceFile.getName().endsWith(GlobalConfig.SECRET_FILE_SUFFIX)) {
				final String fileName = resourceFile.getName();
				builder.append(encodeFile(resourceFile))
						.append(GlobalConfig.SECRET_START_TOKEN)
						.append(fileName)
						.append(GlobalConfig.SECRET_END_TOKEN);
				log.info("加密文件：{}", fileName);
			}
		}
		// 拼接加密文件的路径
		Path secretFilePath = Paths.get(dirPath + File.separator
				+ resourceType.toString().toLowerCase() + "."
				+ GlobalConfig.SECRET_FILE_SUFFIX);
		String secretContent = builder.toString();

		// 二次加固并写入
		if (!secretContent.isEmpty()) {
			try {
				Files.write(secretFilePath, aesEncrypt(builder.toString()));
			} catch (IOException e) {
				log.error("加密文件写入失败", e);
			}
		}
	}

	private static String encodeFile(File file) {
		try {
			final byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			throw new AsoulException("资源加密失败", e);
		}
	}

	private static byte[] aesEncrypt(String content) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(GlobalConfig.SECRET_KEY.getBytes(), GlobalConfig.SECRET_METHOD);
			Cipher cipher = Cipher.getInstance(GlobalConfig.CIPHER_INSTANCE);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(GlobalConfig.SECRET_IV.getBytes()));
			return cipher.doFinal(content.getBytes());
		} catch (Exception e) {
			throw new AsoulException("aes加密失败", e);
		}
	}
}
