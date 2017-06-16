package rbac.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by pandaking on 2017/6/16.
 */
@Slf4j
@Component
public class UploadService {

    @Value("${upload.path}")
    private String uploadPath;


    public byte[] getUploadFile(String filename) throws IOException {
        InputStream stream;
        File file = new File(String.format("%s/%s", uploadPath, filename));
        if (!file.isFile() || !file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        stream = new FileInputStream(file);
        byte[] data = new byte[stream.available()];
        stream.read(data);
        return data;
    }


    public boolean saveUploadFile(String filename, InputStream fileInputStream) {
        String fullFileName = String.format("%s/%s", uploadPath, filename);
        String folderName = fullFileName.substring(0, fullFileName.lastIndexOf("/"));
        // 创建folder
        {
            File folder = new File(folderName);
            if (!folder.exists() || !folder.isDirectory()) {
                if (!folder.mkdirs()) {
                    log.error("创建文件夹失败 {}", folderName);
                    return false;
                }
            }
        }
        File file = new File(fullFileName);
        byte data[];
        try {
            InputStream stream = new BufferedInputStream(fileInputStream);
            data = new byte[stream.available()];
            stream.read(data);
            stream.close();

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("保存文件失败", e);
            return false;
        }
        return true;
    }

}
