package com.txu.job.excel;


import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
//参数excel文件流，sheet的index，标题第几行也就是生成的map->key建议写死，
//开始行数，读取结束行数。
public class ExcelUtil {
    public static List<Map<Integer, Object>> readeExcelData(InputStream excelInputSteam,
                                                           String sheetNumber,
                                                           int headerNumber,
                                                           int rowStart, Integer rowEnd) throws IOException, InvalidFormatException {
//        System.out.println("请确保传入参数所有的下标都是行-1的index");
        List<Map<Integer, Object>> result = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(excelInputSteam);
        //XSSFWorkbook workbook = new XSSFWorkbook(excelInputSteam);
        Sheet sheet = workbook.getSheet(sheetNumber);
        Row header = sheet.getRow(headerNumber);
        Row lastHeader = sheet.getRow(headerNumber+1);
        //最后一行数据
//        System.out.println("整个sheet(index="+sheetNumber+")最后一行index="+sheet.getLastRowNum());
        if(rowEnd==null){
            rowEnd=sheet.getLastRowNum();
            System.out.println("传入结束行号为空，则读取开始行后的所有行");
        }
//        System.out.println("实际目标读取最后一行index="+rowEnd);
        DataFormatter dataFormatter = new DataFormatter();
        //获取标题信息
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            headers.add(dataFormatter.formatCellValue(cell));
        }
        System.out.println("head size="+headers.size());
        //获取内容信息
        for (int i = rowStart; i <= rowEnd; i++) {
            Row currentRow = sheet.getRow(i);
            if (Objects.isNull(currentRow)) {
                continue;
            }
            Map<Integer, Object> dataMap = new TreeMap<>();
//            for (int j = 0; j < header.getLastCellNum(); j++) {
            //第一行可能为不完全的合并单元格，根据第二行数量来处理
            for (int j = 0; j < lastHeader.getLastCellNum(); j++) {
                //将null转化为Blank
                Cell data = currentRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (Objects.isNull(data)) {     //感觉这个if有点多余
                    dataMap.put(j, null);
                } else {
                    dataMap.put(j, getCellFormatValue(data));
                }
            }
            result.add(dataMap);
        }
        excelInputSteam.close();
        return result;
    }


    public static String getCellFormatValue(Cell cell){
        String cellValue = "";
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellTypeEnum()){
                case NUMERIC:{
                    double tmp = cell.getNumericCellValue();
                    if (isInt(tmp)) {
                        cellValue = String.valueOf((int)tmp);
                    } else {
                        cellValue = String.valueOf(tmp);
                    }
                    break;
                }
                case ERROR:
                    cellValue="";
                    break; // 错误类型
                case _NONE:
                    cellValue="";
                    break;
                case BLANK:
                    cellValue="";
                    break;
                case FORMULA:{
                    try {
                        BigDecimal decimal=new BigDecimal(cell.getNumericCellValue());
                        cellValue = String.valueOf(decimal.setScale(4,BigDecimal.ROUND_HALF_UP));
                    } catch (IllegalStateException e) {
                        try {
                            cellValue = String.valueOf(cell.getRichStringCellValue());
                        }catch (IllegalStateException e1) {
                            cellValue="";
                        }
                    }
                    break;
                }
                case STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue.trim();
    }
    public static boolean isInt(double num)
    {
        return Math.abs(num - Math.round(num)) < Double.MIN_VALUE;
    }

    public static void transitionPng(String path, com.aspose.cells.Workbook wb) throws Exception{
        Worksheet sheet1 = wb.getWorksheets().get(0);
        Worksheet sheet2 = wb.getWorksheets().get(1);
        transitionExcel(path+"_1.png", sheet1);
        transitionExcel(path+"_2.png", sheet2);
    }

    public static void transitionExcel(String path,Worksheet sheet) throws Exception{


        //用于存储excel转换的图片

        File pngFile = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pngFile);

            //获取图片写入对象
            ImageOrPrintOptions imgOption = new ImageOrPrintOptions();
            //imgOption.setImageFormat(ImageFormat.getJpeg());
            imgOption.setCellAutoFit(true);
            imgOption.setOnePagePerSheet(true);
            //将sheet写入到图片对象中
            SheetRender render = new SheetRender(sheet, imgOption);
            //将图片写入到输出文件中
            render.toImage(0, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

