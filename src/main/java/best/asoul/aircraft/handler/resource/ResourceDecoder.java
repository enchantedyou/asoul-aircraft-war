package best.asoul.aircraft.handler.resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Matcher;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import best.asoul.aircraft.config.GlobalConfig;
import best.asoul.aircraft.exception.AsoulException;
import best.asoul.aircraft.invoker.ResourceDecodeInvoker;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 资源解密处理器
 * @Author Enchantedyou
 * @Date 2021年12月23日-13:28
 */
@Slf4j
public class ResourceDecoder {

    private ResourceDecoder(){
    }

    /**
     * @Description 资源文件解密
     * @Author Enchantedyou
     * @Date 2021/12/23-13:44
     * @param encodeFile
     * @param invoker
     */
    public static void decode(File encodeFile, ResourceDecodeInvoker invoker){
        if(!encodeFile.exists() || !encodeFile.isFile()
                || !encodeFile.getName().toLowerCase().endsWith(GlobalConfig.SECRET_FILE_SUFFIX)){
            log.warn("加密文件不合法");
            return;
        }

        try{
            //字节码解密
            final byte[] encBytes = Files.readAllBytes(Paths.get(encodeFile.getPath()));
            final String encodeContent = new String(aesDecrypt(encBytes));
            //文件拆分
            final Matcher matcher = GlobalConfig.SECRET_RESOURCE_SPLIT_PATTERN.matcher(encodeContent);
            int prevIndexOf = 0;
            while(matcher.find()){
                final String group = matcher.group();
                int currentIndexOf = encodeContent.indexOf(group);
                String fileName = group.replace(GlobalConfig.SECRET_START_TOKEN, "")
                        .replace(GlobalConfig.SECRET_END_TOKEN, "");
                final byte[] decode = Base64.getDecoder().decode(encodeContent.substring(prevIndexOf, currentIndexOf));
                prevIndexOf = currentIndexOf + group.length();
                //对解密的文件流进行回调处理
                invoker.invoke(fileName, new ByteArrayInputStream(decode));
            }
        }catch (Exception e){
            log.error("资源解密失败");
        }
    }

    private static byte[] aesDecrypt(byte[] encBytes) {
        try {
            byte[] raw = GlobalConfig.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(raw, GlobalConfig.SECRET_METHOD);
            Cipher cipher = Cipher.getInstance(GlobalConfig.CIPHER_INSTANCE);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(GlobalConfig.SECRET_IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(encBytes);
        } catch (Exception e) {
            throw new AsoulException("aes解密失败", e);
        }
    }
}
