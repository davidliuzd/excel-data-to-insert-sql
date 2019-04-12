package net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.service.ReadExcelService;
import net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.util.POIUtil;
import net.liuzd.exceldatatoinsertsql.exceldatatoinsertsql.util.Result;

@RestController
public class UploadExcelController {

    @Autowired
    private ReadExcelService readExcelService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Result<String>> upload(@RequestParam("tableName") String tableName,
            @RequestParam("columns") String columns, @RequestParam("file") MultipartFile file,
            @RequestParam(name = "insertBath", required = false, defaultValue = "false") Boolean insertBath)
            throws Exception {
        //
        if (StringUtils.isBlank(tableName)) {
            return to(false, "表名不能为空！");
        }
        if (StringUtils.isBlank(columns)) {
            return to(false, "列名不能为空！");
        }
        if (file.isEmpty()) {
            return to(false, "上传失败，请选择文件");
        }
        //
        if (!POIUtil.isExcel(file)) {
            return to(false, "请上传Excel文件");
        }
        return to(true, readExcelService.convertInsertSql(tableName, columns, insertBath, file));
    }

    private ResponseEntity<Result<String>> to(boolean isSuccess, String data) {
        return ResponseEntity.ok().body(new Result<String>(isSuccess, isSuccess ? 200 : 500, isSuccess ? "success"
                : "fail", data));
    }

}
