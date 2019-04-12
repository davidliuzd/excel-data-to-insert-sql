出错1：
org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException: No valid entries or contents found, this is not a valid OOXML (Office Open XML) file
	at org.apache.poi.openxml4j.opc.ZipPackage.getPartsImpl(ZipPackage.java:286)
	at org.apache.poi.openxml4j.opc.OPCPackage.getParts(OPCPackage.java:756)
	at org.apache.poi.openxml4j.opc.OPCPackage.open(OPCPackage.java:327)
	at com.alibaba.excel.analysis.v07.XlsxSaxAnalyser.<init>(XlsxSaxAnalyser.java:44)
	at com.alibaba.excel.analysis.ExcelAnalyserImpl.getSaxAnalyser(ExcelAnalyserImpl.java:31)
	at com.alibaba.excel.analysis.ExcelAnalyserImpl.analysis(ExcelAnalyserImpl.java:53)
	at com.alibaba.excel.ExcelReader.read(ExcelReader.java:92)
解决：new BufferedInputStream(InputStream stream);
参见：https://github.com/alibaba/easyexcel/issues/115


处理2：删除集合中的空值 https://www.baeldung.com/java-remove-nulls-from-list 
