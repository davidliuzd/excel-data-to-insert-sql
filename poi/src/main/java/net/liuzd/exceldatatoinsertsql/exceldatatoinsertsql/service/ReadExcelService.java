package net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.service;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.util.POIUtil;

@Slf4j
@Service
public class ReadExcelService {

    /**
     * @Title: convertSql
     * @Description: 读取Excel并转换为SQL
     * @param tableName
     * @param columns
     * @param insertBath
     * @param file
     * @return String
     * @throws Exception
     */
    public String convertInsertSql(String tableName, String columns, boolean insertBath, MultipartFile file)
            throws Exception {
        //
        String templateSql = POIUtil.getSql(tableName, columns, insertBath);
        //
        Sheet sheet = POIUtil.getFirst(file);
        //
        int tempatInt = 0;
        int lastRowNum = sheet.getLastRowNum();
        Row firstRow = sheet.getRow(tempatInt);
        int cols = tempatInt;
        if (null != firstRow) {
            cols = firstRow.getPhysicalNumberOfCells();
        }
        log.info("总行：{} ，总列数：{}", lastRowNum, cols);
        //
        Iterator<Row> iterator = sheet.iterator();
        //
        StringBuilder sbr = new StringBuilder();
        String tempSp = ",";
        //
        while (iterator.hasNext()) {
            Row row = iterator.next();
            String columnVals = POIUtil.readRow(row, cols, tempSp);
            String sql = null;
            if (insertBath) {
                sql = String.format("(%s)" + tempSp, columnVals);
                sbr.append(sql);
            } else {
                sql = StringUtils.replace(templateSql, "?", columnVals);
            }        
            sbr.append(sql);
        }
        String sql = sbr.toString();
        if (insertBath) {
            String tempSql = StringUtils.removeEnd(sql, tempSp);
            sql =StringUtils.replace(templateSql, "?", tempSql);
        }
        return sql;
    }

}
