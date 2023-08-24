package com.txu.job.excel;

/**
 * @version v1.0
 * @date 2022/9/14
 * @desc
 * @Since 2022/9/14 17:11
 **/
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Excel 工具类
 *
 * @author liudong
 * @date 2020-09-07
 */
public class ExcelWriter {
    public static final int FONT_FIRST_LEVEL = 28; // 一级字体
    public static final int FONT_SECOND_LEVEL = 14;// 二级字体
    public static final int FONT_THIRD_LEVEL = 7; // 三级字体
    public static final int LEFT = 1; // 左对齐
    public static final int CENTER = 2; // 居中
    public static final int RIGHT = 3; // 右对齐
    public static final int TOP = 1; // 左对齐
    public static final int BOTTOM = 3; // 右对齐
    private Workbook wb = null;
    private Sheet sheet = null;
    private FileOutputStream pos = null;

    public ExcelWriter(String filePath) {
        try {
            pos = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ExcelWriter(String filePath, String sheetName) {
        try {
            createSheet(sheetName);
            pos = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建表格
     *
     * @param sheetName 表格名称
     * @return
     */
    public void createSheet(String sheetName) {
        //创建工作簿  HSSFWorkbook -- 2003
        this.wb = new XSSFWorkbook(); //2007版本
        this.sheet = wb.createSheet(sheetName);
    }

    /**
     * 创建多个sheet表格
     *
     * @param wb
     * @param sheetIndex
     * @param sheetName
     */
    public void createSheet(Workbook wb, Integer sheetIndex, String sheetName) {
        this.wb = wb;
        this.sheet = this.wb.createSheet();
        this.wb.setSheetName(sheetIndex, sheetName);
    }

    public Sheet getSheet() {
        return this.sheet;
    }

    /**
     * 获取最后一个不为空的行号
     *
     * @return
     */
    public int getLastNotBlankRowIndex() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                return i - 1;
            }
        }
        return -1;
    }

    /**
     * 获取首个空白行号
     *
     * @return
     */
    public int getFirstBlankRowIndex() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取单个单元格
     *
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public Cell getCell(int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        return row.getCell(colIndex);
    }

    /**
     * 获取多个单元格
     *
     * @param rowIndexArr
     * @param colIndexArr
     * @return
     */
    public List<Cell> getCells(int[] rowIndexArr, int[] colIndexArr) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < rowIndexArr.length; i++) {
            Row row = sheet.getRow(rowIndexArr[i]);
            for (int j = 0; j < colIndexArr.length; j++) {
                cells.add(row.getCell(colIndexArr[j]));
            }
        }
        return cells;
    }

    /**
     * 获取多个单元格
     *
     * @param rowIndex
     * @param colIndexArr
     * @return
     */
    public List<Cell> getCells(int rowIndex, int... colIndexArr) {
        int[] rows = {rowIndex};
        return getCells(rows, colIndexArr);
    }

    /**
     * 设置单元格样式
     *
     * @param cellStyle 样式
     * @param cell      单元格
     * @return
     */
    public void setCellStyle(CellStyle cellStyle, Cell cell) {
        setCellStyle(cellStyle, cell, -1, null);
    }

    /**
     * 设置单元格样式
     *
     * @param cellStyle 样式
     * @param cells     单元格列表
     * @return
     */
    public void setCellStyle(CellStyle cellStyle, List<Cell> cells) {
        cells.forEach(e -> setCellStyle(cellStyle, e, -1, null));
    }

    /**
     * 设置单元格样式
     *
     * @param cellStyle       样式
     * @param cell            单元格
     * @param colIndex        列索引
     * @param styleEnableCols 需要开启样式的列标
     * @return
     */
    public void setCellStyle(CellStyle cellStyle, Cell cell, int colIndex, Set<Integer> styleEnableCols) {
        if (styleEnableCols != null && styleEnableCols.size() != 0) {
            if (styleEnableCols.contains(colIndex)) {
                cell.setCellStyle(cellStyle);
            }
        } else {
            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * 设置单元格列宽
     *
     * @param widthValue      列宽值
     * @param sheet           表格
     * @param colIndex        列索引
     * @param widthEnableCols 需要开启列宽的列标
     * @return
     */
    public void setColWidth(int widthValue, Sheet sheet, int colIndex, Set<Integer> widthEnableCols) {
        if (widthEnableCols != null && widthEnableCols.size() != 0) {
            if (widthEnableCols.contains(colIndex)) {
                sheet.setColumnWidth(colIndex, widthValue);
            }
        } else {
            sheet.setColumnWidth(colIndex, widthValue);
        }
    }

    /**
     * 合并单元格
     *
     * @param firstRow 第一行
     * @param lastRow  最后一行
     * @param firstCol 第一列
     * @param lastCol  最后一列
     */
    public void merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        merge(firstRow, lastRow, firstCol, lastCol, null, null, 0, 0, null, null);
    }

    /**
     * 合并单元格
     *
     * @param firstRow 第一行
     * @param lastRow  最后一行
     * @param firstCol 第一列
     * @param lastCol  最后一列
     * @param content  内容
     */
    public void merge(int firstRow, int lastRow, int firstCol, int lastCol, String content) {
        merge(firstRow, lastRow, firstCol, lastCol, content, null, 0, 0, null, null);
    }

    /**
     * 合并单元格
     *
     * @param firstRow  第一行
     * @param lastRow   最后一行
     * @param firstCol  第一列
     * @param lastCol   最后一列
     * @param content   内容
     * @param cellStyle 单元格样式
     */
    public void merge(int firstRow, int lastRow, int firstCol, int lastCol, String content, CellStyle cellStyle) {
        merge(firstRow, lastRow, firstCol, lastCol, content, cellStyle, 0, 0, null, null);
    }

    /**
     * 合并单元格
     *
     * @param firstRow  第一行
     * @param lastRow   最后一行
     * @param firstCol  第一列
     * @param lastCol   最后一列
     * @param content   内容
     * @param cellStyle 单元格样式
     * @param rowHeight 行高
     * @param colWidth  列宽
     */
    public void merge(int firstRow, int lastRow, int firstCol, int lastCol, String content, CellStyle cellStyle, int rowHeight, int colWidth) {
        merge(firstRow, lastRow, firstCol, lastCol, content, cellStyle, rowHeight, colWidth, null, null);
    }

    /**
     * 合并单元格
     *
     * @param firstRow  第一行
     * @param lastRow   最后一行
     * @param firstCol  第一列
     * @param lastCol   最后一列
     * @param content   内容
     * @param cellStyle 单元格样式
     * @param rowHeight 行高
     * @param colWidth  列宽
     */
    public void merge(int firstRow, int lastRow, int firstCol, int lastCol, String content, CellStyle cellStyle, int rowHeight, int colWidth, Set<Integer> styleEnableCols, Set<Integer> widthEnableCols) {
        if (firstRow == lastRow && firstCol == lastCol) {
            return;
        }
        if (firstRow > lastRow || firstCol > lastCol) {
            throw new RuntimeException("The first row(or column) cannot be greater than the last column!");
        }
        Row row;
        Cell cell;
        for (int i = firstRow; i <= lastRow; i++) {
            if (sheet.getRow(i) != null) {
                row = sheet.getRow(i);
            } else {
                row = sheet.createRow(i);
            }
            if (rowHeight != 0) {
                row.setHeightInPoints(rowHeight);// 设置行高
            }
            for (int j = firstCol; j <= lastCol; j++) {
                if (row.getCell(j) != null) {
                    cell = row.getCell(j);
                } else {
                    cell = row.createCell(j);
                }
                // 设置单元格值
                if (content != null) {
                    cell.setCellValue(content);
                }
                // 设置单元格样式
                if (cellStyle != null) {
                    setCellStyle(cellStyle, cell, j, styleEnableCols);
                }
                // 设置列宽
                if (colWidth != 0) {
                    setColWidth(colWidth, sheet, j, widthEnableCols);
                }
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 数据写入
     *
     * @param data 数据源
     */
    public void write(String data) {
        write(data, null, null, 0, 0, null, null);
    }

    /**
     * 数据写入
     *
     * @param data      数据源
     * @param cellStyle 单元格样式
     */
    public void write(String data, CellStyle cellStyle) {
        write(data, cellStyle, cellStyle, 0, 0, null, null);
    }

    /**
     * 数据写入
     *
     * @param data      数据源
     * @param rowHeight 行高
     * @param colWidth  列宽
     */
    public void write(String data, int rowHeight, int colWidth) {
        write(data, null, null, rowHeight, colWidth, null, null);
    }

    /**
     * 数据写入
     *
     * @param data            数据源
     * @param cellStyle       单元格样式
     * @param styleEnableCols 需要开启样式的列标
     */
    public void write(String data, CellStyle cellStyle, Set<Integer> styleEnableCols) {
        write(data, cellStyle, cellStyle, 0, 0, styleEnableCols, null);
    }

    /**
     * 数据写入
     *
     * @param data            数据源
     * @param colWidth        列宽
     * @param widthEnableCols 需要开启列宽的列标
     */
    public void write(String data, int colWidth, Set<Integer> widthEnableCols) {
        write(data, null, null, 0, colWidth, null, widthEnableCols);
    }

    /**
     * 数据写入
     *
     * @param data      数据源
     * @param cellStyle 单元格样式
     * @param rowHeight 行高
     * @param colWidth  列宽
     */
    public void write(String data, CellStyle cellStyle, CellStyle otherCellStyle, int rowHeight, int colWidth, Set<Integer> styleEnableCols, Set<Integer> widthEnableCols) {
        int startRow = getFirstBlankRowIndex();
        Row row = sheet.createRow(startRow);

        CellStyle titleStyle = titleStyle(wb);
        CellStyle bodyStyle = bodyStyle(wb);
        if (rowHeight != 0) {
            row.setHeightInPoints(rowHeight);
        }
        String[] d = data.split(",", -1);
        for (int i = 0; i < d.length; i++) {
            Cell cell = row.createCell(i);
            //判断传进来的字符串是不是数值类型,数值类型右对齐
            if (isNumeric(d[i])) {
                cell.setCellValue(Double.valueOf(d[i]));
                cell.setCellStyle(bodyStyle);
            } else {
                String tmp = d[i];
                //最后一列分多行
//                if (i == (d.length-1)) {
//                  int size = tmp.length();
//                  if (size > 8) {
//                      int idx = size/2;
//                      tmp = d[i].substring(0, idx) + "\n" + d[i].substring(idx, size);
//                  }
//                }
                cell.setCellValue(tmp);
                cell.setCellStyle(titleStyle);
            }

            //设置单元格样式

                //最后一列左对齐
                if (i == d.length - 1) {
                    if (otherCellStyle != null) {
                        otherCellStyle.setAlignment(HorizontalAlignment.LEFT);
                        setCellStyle(otherCellStyle, cell, i, styleEnableCols);
                    }
                } else {
                    if (cellStyle != null) {
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        setCellStyle(cellStyle, cell, i, styleEnableCols);
                    }
                }
            // 设置列宽
            if (colWidth != 0 && i < d.length - 1) {
                setColWidth(colWidth, sheet, i, widthEnableCols);
            } else {
                //最后一列自适应列宽
                sheet.autoSizeColumn(i, true);
            }
        }
    }

    /**
     * 数据写入
     *
     * @param data     数据源
     * @param sortRule 排序规则
     */
    public void write(Map<String, Object> data, String sortRule) {
        write(data, sortRule, null, null,0, 0, null, null);
    }

    /**
     * 数据写入
     *
     * @param data      数据源
     * @param sortRule  排序规则
     * @param cellStyle 单元格样式
     */
    public void write(Map<String, Object> data, String sortRule, CellStyle cellStyle) {
        write(data, sortRule, cellStyle, cellStyle,0, 0, null, null);
    }

    /**
     * 数据写入
     *
     * @param data      数据源
     * @param sortRule  排序规则
     * @param cellStyle 单元格样式
     * @param rowHeight 行高
     * @param colWidth  列宽
     */
    public void write(Map<String, Object> data, String sortRule, CellStyle cellStyle, CellStyle otherCellStyle, int rowHeight, int colWidth, Set<Integer> styleEnableCols, Set<Integer> widthEnableCols) {
        StringBuilder sb = new StringBuilder();
        Object value;
        if (StringUtils.isNotEmpty(sortRule)) {
            String[] s = sortRule.split(",");
            for (int i = 0; i < s.length; i++) {
                value = data.get(s[i]) == null ? " " : data.get(s[i]);
                sb.append(value).append(",");
            }
        } else {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                value = entry.getValue() == null ? " " : entry.getValue();
                sb.append(value).append(",");
            }
        }
        write(sb.toString(), cellStyle, otherCellStyle, rowHeight, colWidth, styleEnableCols, widthEnableCols);
    }


    /**
     * 获取单元格样式
     *
     * @return
     */
    public CellStyle getCellStyle() {
        return getCellStyle("宋体", 0, false, 0, 0);
    }

    /**
     * 获取单元格样式
     *
     * @return
     */
    public CellStyle getCellStyle(String fontName) {
        return getCellStyle(fontName, 0, false, 0, 0);
    }

    /**
     * 获取单元格样式
     *
     * @param enableBorder 是否开启边框样式
     * @return
     */
    public CellStyle getCellStyle(boolean enableBorder) {
        return getCellStyle("宋体", 0, enableBorder, 0, 0);
    }

    /**
     * 获取单元格样式
     *
     * @param fontName  字体名称
     * @param fontLevel 字体大小
     * @return
     */
    public CellStyle getCellStyle(String fontName, int fontLevel) {
        return getCellStyle(fontName, fontLevel, false, 0, 0);
    }

    /**
     * 获取单元格样式
     *
     * @param fontName     字体名称
     * @param enableBorder 是否开启边框样式
     * @return
     */
    public CellStyle getCellStyle(String fontName, boolean enableBorder) {
        return getCellStyle(fontName, 0, enableBorder, 0, 0);
    }

    /**
     * 获取单元格样式
     *
     * @param fontName            字体名称
     * @param horizontalAlignment 水平对齐方式
     * @param verticalAlignment   垂直对齐方式
     * @return
     */
    public CellStyle getCellStyle(String fontName, int horizontalAlignment, int verticalAlignment) {
        return getCellStyle(fontName, 0, false, horizontalAlignment, verticalAlignment);
    }

    /**
     * 获取单元格样式
     *
     * @param fontName            字体名称
     * @param fontLevel           字体大小
     * @param enableBorder        是否开启边框
     * @param horizontalAlignment 水平对齐方式
     * @param verticalAlignment   垂直对齐方式
     * @return
     */
    public CellStyle getCellStyle(String fontName, int fontLevel, boolean enableBorder, int horizontalAlignment, int verticalAlignment) {
        CellStyle style = wb.createCellStyle();
        if (enableBorder) {
            style.setBorderTop(BorderStyle.THIN);//上边框
            style.setBorderBottom(BorderStyle.THIN);//下边框
            style.setBorderLeft(BorderStyle.THIN);//左边框
            style.setBorderRight(BorderStyle.THIN);//右边框

        }
        style.setWrapText(true);//自动换行
        //创建字体对象
        Font font = wb.createFont();
        font.setFontName(fontName); //字体
        if (fontLevel != 0) {
            font.setFontHeightInPoints((short) fontLevel);
            font.setBold(true);//加粗
        }
        style.setFont(font);
        //水平对齐
        switch (horizontalAlignment) {
            case LEFT:
                style.setAlignment(HorizontalAlignment.LEFT);//水平居中
                break;
            case RIGHT:
                style.setAlignment(HorizontalAlignment.RIGHT);//水平居中
                break;
            default:
                style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        }
        switch (verticalAlignment) {
            case TOP:
                style.setVerticalAlignment(VerticalAlignment.TOP);//顶端对齐
                break;
            case BOTTOM:
                style.setVerticalAlignment(VerticalAlignment.BOTTOM);//底部对齐
                break;
            default:
                style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        }
        return style;
    }

    /**
     * @param str 判断数据源类型
     **/
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 设置表体字体边框格式
     */
    public static CellStyle bodyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置单元格字体
        Font headerFont = workbook.createFont(); // 字体
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setFontName("宋体");
        style.setFont(headerFont);
        style.setWrapText(true);

        // 设置单元格边框及颜色
        style.setBorderBottom(BorderStyle.valueOf((short) 1));
        style.setBorderLeft(BorderStyle.valueOf((short) 1));
        style.setBorderRight(BorderStyle.valueOf((short) 1));
        style.setBorderTop(BorderStyle.valueOf((short) 1));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setWrapText(true);
        return style;
    }

    /**
     * 设置表头字体边框格式
     */
    public static CellStyle titleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置单元格字体
        Font headerFont = workbook.createFont(); // 字体
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setFontName("宋体");
        style.setFont(headerFont);
        style.setWrapText(true);

        // 设置单元格边框及颜色
        style.setBorderBottom(BorderStyle.valueOf((short) 1));
        style.setBorderLeft(BorderStyle.valueOf((short) 1));
        style.setBorderRight(BorderStyle.valueOf((short) 1));
        style.setBorderTop(BorderStyle.valueOf((short) 1));
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    /**
     * 将内容写入文件
     *
     * @throws IOException
     */
    public void flush() throws IOException {
        wb.write(pos);
    }

    /**
     * 关闭流
     *
     * @throws IOException
     */
    public void close() throws IOException {
        pos.close();
    }

    public static void main(String[] args) throws IOException {
        ExcelWriter writer = new ExcelWriter("D:/rpt/number/a.xlsx");

        Workbook wb = new XSSFWorkbook();
        writer.createSheet(wb, 0, "test");

        //创建多个sheet
        /*writer.write("a,b,c");
        writer.write("2,3.77,4");
        writer.write("2,7.33,4");

        writer.createSheet(wb, 1, "test1");
        writer.write("1,2,3");

        writer.createSheet(wb, 2, "test2");
        writer.write("x,y,z");*/

        //编辑格式样式
        CellStyle cellStyle = writer.getCellStyle("黑体", true);
        Integer[] arr2 = new Integer[]{0, 6};
        Set<Integer> styleCellCols = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> widthCellCols = new HashSet<>(Arrays.asList(arr2));

        writer.merge(0, 0, 0, 6, "哈哈哈", cellStyle, 50, 30 * 256, null, styleCellCols);
        writer.write("a,b,c,d,e,f,g", cellStyle, styleCellCols);
        writer.write("a,b,c,d,e,f,g", 15 * 256, widthCellCols);
        writer.write("1,2,3,4,5,6,7.99"); //测试数值类型是不是右对齐的

        writer.flush();
        writer.close();
    }
}
