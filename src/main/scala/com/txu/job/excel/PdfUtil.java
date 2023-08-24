package com.txu.job.excel;

/**
 * @version v1.0
 * @date 2022/9/14
 * @desc
 * @Since 2022/9/14 16:50
 **/
import com.aspose.cells.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextMarginFinder;
import scala.Array;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashSet;

public class PdfUtil {

    public static void main(String[] args) {
        HashSet a = new HashSet<Integer>();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);
        HashSet b = new HashSet<Integer>();
        b.add(1);
        b.add(5);
        b.add(6);
        b.add(4);

    }


    public static long fileWrite(String filePath, String content, int index) {
        File file = new File(filePath);
        RandomAccessFile randomAccessTargetFile;
        MappedByteBuffer map;
        try {
            randomAccessTargetFile = new RandomAccessFile(file, "rw");

            FileChannel targetFileChannel = randomAccessTargetFile.getChannel();
            map = targetFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, (long) 1024 * 1024 * 1024);
            map.position(index);
            map.put(content.getBytes());
            return map.position();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return 0L;
    }

    public static String fileRead(String filePath, long index) {
        File file = new File(filePath);
        RandomAccessFile randomAccessTargetFile;
        MappedByteBuffer map;
        try {
            randomAccessTargetFile = new RandomAccessFile(file, "rw");
            FileChannel targetFileChannel = randomAccessTargetFile.getChannel();
            map = targetFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, index);
            byte[] byteArr = new byte[10 * 1024];
            map.get(byteArr, 0, (int) index);
            return new String(byteArr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return "";
    }
    static class WFArray  {
            String[] a ;
            public WFArray(String[] a){
                this.a =a;
            }

        @Override
        public boolean equals(Object o) {
                String[] b = ((WFArray) o).a;
                for (int i=0;i<a.length;i++){
                    if (a[i] != b[i])
                        return false;
                }
                return true;
         }
    }

    /**
     * excel 转 pdf
     *
     * @param excelFilePath excel文件路径
     */
    public static void excel2pdf(String excelFilePath) {
        excel2pdf(excelFilePath, null, null);
    }

    /**
     * excel 转 pdf
     *
     * @param excelFilePath excel文件路径
     * @param convertSheets 需要转换的sheet
     */
    public static void excel2pdf(String excelFilePath, int[] convertSheets) {
        excel2pdf(excelFilePath, null, convertSheets);
    }

    /**
     * excel 转 pdf
     *
     * @param excelFilePath excel文件路径
     * @param pdfFilePath   pdf文件路径
     */
    public static void excel2pdf(String excelFilePath, String pdfFilePath) {
        excel2pdf(excelFilePath, pdfFilePath, null);
    }

    /**
     * excel 转 pdf
     *
     * @param excelFilePath excel文件路径
     * @param pdfFilePath   pdf文件路径
     * @param convertSheets 需要转换的sheet
     */
    public static void excel2pdf(String excelFilePath, String pdfFilePath, int[] convertSheets) {
        try {
            pdfFilePath = pdfFilePath == null ? getPdfFilePath(excelFilePath) : pdfFilePath;
            // 验证 License
            getLicense();
            Workbook wb = new Workbook(excelFilePath);
            FileOutputStream fileOS = new FileOutputStream(pdfFilePath);
            PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
            pdfSaveOptions.setOnePagePerSheet(true);

            if (null != convertSheets) {
                printSheetPage(wb, convertSheets);
            }
//            pdfSaveOptions.setCompliance(PdfCompliance.NONE);
            wb.save(fileOS, pdfSaveOptions);
            fileOS.flush();
            fileOS.close();
            System.out.println("convert success");
        } catch (Exception e) {
            System.out.println("convert failed");
            e.printStackTrace();
        }
    }

    /**
     * 获取 生成的 pdf 文件路径，默认与源文件同一目录
     *
     * @param excelFilePath excel文件
     * @return 生成的 pdf 文件
     */
    private static String getPdfFilePath(String excelFilePath) {
        return excelFilePath.split(".xls")[0] + ".pdf";
    }

    /**
     * 获取 license 去除水印
     * 若不验证则转化出的pdf文档会有水印产生
     */
    private static void getLicense() {
        String licenseFilePath = "excel-license.xml";
        try {
            InputStream is = PdfUtil.class.getClassLoader().getResourceAsStream(licenseFilePath);
            License license = new License();
            license.setLicense(is);
        } catch (Exception e) {
            System.out.println("license verify failed");
            e.printStackTrace();
        }
    }

    /**
     * 隐藏workbook中不需要的sheet页。
     *
     * @param sheets 显示页的sheet数组
     */
    private static void printSheetPage(Workbook wb, int[] sheets) {
        for (int i = 1; i < wb.getWorksheets().getCount(); i++) {
            wb.getWorksheets().get(i).setVisible(false);
        }
        if (null == sheets || sheets.length == 0) {
            Worksheet sheet1 = wb.getWorksheets().get(1);
            sheet1.setVisible(true);
            Worksheet sheet = wb.getWorksheets().get(0);
            sheet.setVisible(false);
            try {
                setColumnWithAuto(sheet1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            for (int i = 0; i < sheets.length; i++) {
                wb.getWorksheets().get(i).setVisible(true);
            }
        }
    }

    public static void setColumnWithAuto(Worksheet sheet) throws Exception {
        Cells cells = sheet.getCells();
        int columnCount = cells.getMaxColumn();  //获取表页的最大列数
        int rowCount = cells.getMaxRow();        //获取表页的最大行数

        for (int col = 0; col < columnCount; col++)
        {
            sheet.autoFitColumn(col, 0, rowCount);
        }
        for (int col = 0; col < columnCount; col++)
        {
            cells.setColumnWidthPixel(col, cells.getColumnWidthPixel(col) + 30);
        }
    }

    public static void cutWilteSpace(String path) {


        PdfStamper stamper = null;
        try {
            PdfReader reader = new PdfReader(path);
            stamper = new PdfStamper(reader, new FileOutputStream(path.split("\\.pdf")[0]+ "_1.pdf"));


// Go through all pages

        int n = reader.getNumberOfPages();

        for (int i = 1; i <= n; i++)

        {

            Rectangle pageSize = reader.getPageSize(i);

            Rectangle rect = getOutputPageSize(pageSize, reader, i);

            PdfDictionary page = reader.getPageN(i);

            page.put(PdfName.CROPBOX, new PdfArray(new float[]{rect.getLeft()-20, rect.getBottom()-11, rect.getRight()+4, rect.getTop()+18}));

            stamper.markUsed(page);

        }
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Rectangle getOutputPageSize(Rectangle pageSize, PdfReader reader, int page) throws IOException

    {

        PdfReaderContentParser parser = new PdfReaderContentParser(reader);

        TextMarginFinder finder = parser.processContent(page, new TextMarginFinder());

        Rectangle result = new Rectangle(finder.getLlx(), finder.getLly(), finder.getUrx(), finder.getUry());

        System.out.printf("Text/bitmap boundary: %f,%f to %f, %f\n", finder.getLlx(), finder.getLly(), finder.getUrx(), finder.getUry());

        return result;

    }
}
