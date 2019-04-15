package net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        Map<String, Integer> columnsLengMap = new HashMap<>();
        String[] arraysColumns = StringUtils.split(columns,",");
        //
        while (iterator.hasNext()) {
            Row row = iterator.next();
            // String columnVals = POIUtil.readRow(row, cols, tempSp);

            StringBuilder cellValus = new StringBuilder();
            //
            for (int i = 0; i < cols; i++) {
                //
                Integer length = columnsLengMap.get(arraysColumns[i]);
                if (null == length) {
                    length = 0;
                }
                //
                Object cellValue = POIUtil.getCellVal(row.getCell(i));
                if (cellValue instanceof String) {
                    cellValus.append("'").append(cellValue).append("'").append(tempSp);
                    //
                    Integer colSize = ((String)cellValue).length();                    
                    if(length < colSize) {
                        length =colSize;            
                        columnsLengMap.put(arraysColumns[i], length);
                    }
                } else {
                    cellValus.append(cellValue).append(tempSp);                    
                    //
                }                
                //
            }
            //
            String columnVals = StringUtils.removeEnd(cellValus.toString(), tempSp);
            //
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
            sql = StringUtils.replace(templateSql, "?", tempSql);
        }
        return sql;
    }

}
