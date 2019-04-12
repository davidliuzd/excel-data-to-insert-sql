## 用于将Excel中的数据转换为Insert语句


https://www.cnblogs.com/pcheng/p/7485979.html

首先，POI提供了HSSF、XSSF以及SXSSF三种方式操作Excel。他们的区别如下：

HSSF：是操作Excel97-2003版本，扩展名为.xls。

XSSF：是操作Excel2007版本开始，扩展名为.xlsx。

SXSSF：是在XSSF基础上，POI3.8版本开始提供的一种支持低内存占用的操作方式，扩展名为.xlsx。

 其次，大家需要了解下Excel不同版本的一些区别，这些限制其实间接的局限了POI提供的API功能。

1、支持的行数、列数

Excel97-2003版本，一个sheet最大行数65536，最大列数256。

Excel2007版本开始，一个sheet最大行数1048576，最大列数16384。

2、文件大小

.xlsx文件比.xls的压缩率高，也就是相同数据量下，.xlsx的文件会小很多。

3、兼容性

Excel97-2003版本是不能打开.xlsx文件的。

Excel2007开始的版本是可以打开.xls文件的。

SXSSF是一种低内存占用的操作方式，因为其提供了一个新的方法：

SXSSFWorkbook w3= new SXSSFWorkbook(100);//内存中保留100条数据，其余写入硬盘临时文件
在数据量超过设置的值时，会在硬盘生成临时文件保存之前的数据，而且POI会根据规则自动删除生成的这些临时文件。

SXSSF是对XSSF的一种流式扩展，特点是采用了滑动窗口的机制，低内存占用，主要用于数据量非常大的电子表格而虚拟机堆有限的情况。
SXSSFWorkbook提供了一种低内存占用的EXCEL导出方法，但是没有提供读取文件流的方法。因此读入大数据量的时候还是只能使用XSSFWorkbook来读取。

http://www.cnblogs.com/wshsdlau/p/5643847.html
POI Sax 事件驱动解析Excel2007文件

excel工具类
https://github.com/alibaba/easyexcel



