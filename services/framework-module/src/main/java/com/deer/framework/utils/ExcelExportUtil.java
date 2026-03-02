package com.deer.framework.utils;

import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class ExcelExportUtil {

    @SneakyThrows
    public static <T> void export(HttpServletResponse response,
                                  String fileName,
                                  String sheetName,
                                  Class<T> clazz,
                                  List<T> dataList) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedFilename = Base64.getEncoder().encodeToString((fileName + ".xlsx").getBytes(StandardCharsets.UTF_8));
            response.setHeader("filename", encodedFilename);

            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .doWrite(dataList);
        }catch (IOException e){
            throw new IOException("导出失败", e);
        }


    }

}
