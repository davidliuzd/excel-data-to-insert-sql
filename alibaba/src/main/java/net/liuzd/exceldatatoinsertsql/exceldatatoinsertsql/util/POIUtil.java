package net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.util;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class POIUtil {

    public static boolean isXlsx(MultipartFile file) {
        String extension = getFileExtension(file);
        return isXlsx(extension);
    }

    public static boolean isXlsx(String extension) {
        return StringUtils.endsWith(extension, "xlsx");
    }

    public static boolean isXls(String extension) {
        return StringUtils.endsWith(extension, "xls");
    }

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return FilenameUtils.getExtension(fileName);
    }

    public static boolean isExcel(MultipartFile file) {
        String extension = getFileExtension(file);
        return !(isXls(extension) || isXls(extension));
    }

    public static String getSql(String tableName, String columns, boolean insertBath) {
        String insetSql = null;
        if (insertBath) {
            /**
             * 一条INSERT语句插入批量数据的写法： INSERT INTO [表名]([列名],[列名]) VALUES
             * ([列值],[列值])), ([列值],[列值])), ([列值],[列值]));
             */
            insetSql = "INSERT INTO %s (%s) VALUES ?;";

        } else {
            insetSql = "INSERT INTO %s (%s) VALUES (?);";
        }
        return String.format(insetSql, tableName, columns);
    }

    @SuppressWarnings("rawtypes")
    public static String readObject(Object object, String tempSp) {
        StringBuilder cellValus = new StringBuilder();
        @SuppressWarnings("unchecked")
        List<String> row = (List) object;
        if (null != row) {
            // 删除所有空值，为方便这样做，实际要处理下
            row.removeIf(Objects::isNull);
            row.stream().forEach(cellValue -> {
                cellValus.append("'").append(cellValue).append("'").append(tempSp);
            });
        }
        return StringUtils.removeEnd(cellValus.toString(), tempSp);
    }

    public static int getCols(MultipartFile file) throws Exception {
        Sheet sheet = getFirst(file);
        return sheet.getRow(0).getPhysicalNumberOfCells();
    }

    public static Workbook to(MultipartFile file) throws Exception {
        //
        Workbook book = null;
        InputStream is = file.getInputStream();
        //
        try {
            //
            if (!is.markSupported()) {
                is = new PushbackInputStream(is, 8);
            }
            //
            if (isXlsx(file)) {
                book = new XSSFWorkbook(is);
            } else {
                book = new HSSFWorkbook(is);
            }
        } catch (Exception e) {
            log.error("Workbook出错..." + e.getMessage(), e);
            try {
                book = WorkbookFactory.create(is);
            } catch (Exception e2) {
                log.error("使用（WorkbookFactory.create）出错..." + e2.getMessage(), e2);
            }
        }
        return book;
    }

    public static Sheet getFirst(MultipartFile file) throws Exception {
        Workbook wb = to(file);
        return wb.getSheetAt(0);
    }

}
