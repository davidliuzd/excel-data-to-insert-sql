package net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.service;

import java.io.BufferedInputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

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
        ExcelListener listener = new ExcelListener(tableName, columns, insertBath);
        // 解析每行结果在listener中处理
        BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
        new ExcelReader(bis, null, listener).read();;
        //
        return listener.getSql();
    }

    /**
     * 解析监听器， 每解析一行会回调invoke()方法。 整个excel解析结束会执行doAfterAllAnalysed()方法 *
     * 下面只是我写的一个样例而已，可以根据自己的逻辑修改该类。
     */
    @SuppressWarnings("rawtypes")
    public class ExcelListener extends AnalysisEventListener {

        private StringBuilder sbr;
        private boolean       insertBath;
        private String        tempSp;
        private String        templateSql;

        ExcelListener(String tableName, String columns, boolean insertBath) {
            this.templateSql = POIUtil.getSql(tableName, columns, insertBath);
            this.insertBath = insertBath;
            this.sbr = new StringBuilder();
            this.tempSp = ",";
        }

        public void invoke(Object object, AnalysisContext context) {
            log.debug("当前行：" + context.getCurrentRowNum());
            String sql = null;
            String columnVals = POIUtil.readObject(object, tempSp);
            if (insertBath) {
                sql = String.format("(%s)" + tempSp, columnVals);
                sbr.append(sql);
            } else {
                sql = StringUtils.replace(templateSql, "?", columnVals);
            }
            sbr.append(sql);
        }

        public void doAfterAllAnalysed(AnalysisContext context) {
            // datas.clear();//解析结束销毁不用的资源
            log.debug("解析完成...");
        }

        public String getSql() {
            String sql = sbr.toString();
            if (insertBath) {
                String tempSql = StringUtils.removeEnd(sql, tempSp);
                sql = StringUtils.replace(templateSql, "?", tempSql);
            }
            return sql;
        }
    }

}
