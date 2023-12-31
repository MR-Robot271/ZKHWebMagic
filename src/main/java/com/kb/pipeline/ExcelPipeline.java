package com.kb.pipeline;

import com.kb.pojo.Product;
import com.kb.pojo.ProductExcel;
import com.kb.utils.FileUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @Description: 实现写入Excel文件的Pipeline
* @Date: 2023/10/31
*/
public class ExcelPipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        // 存储爬取结果的list
        List<Product> productList=new ArrayList<>();

        // 存储excel文件的实体类list
        List<ProductExcel> productExcelList = new ArrayList<>();

        // 存放结果的excel文件地址
        // 添加时间戳，用于区别不同文件 一次爬虫的结果放在一个文件中
        LocalDateTime localDateTime = LocalDateTime.now();

        // 测试用
//        DateTimeFormatter fileTime = DateTimeFormatter.ofPattern("yyMMddHHmm");
        // 正式用
         DateTimeFormatter fileTime = DateTimeFormatter.ofPattern("yyMMddHH");

        String time = localDateTime.format(fileTime);
        String path = "D:\\CrawlerResult\\ZKHwmCrawlerResult"+time+".xlsx";

        // 暂时存放结果的excel文件地址
        String pathOfTemp = "D:\\CrawlerResult\\ZKHCrawlerTemp.xlsx";

        writeToExcel(resultItems, productExcelList, path, pathOfTemp);
    }

    /**
    * @Description: 将爬虫结果写入Excel文件
    * @Param: [resultItems, productExcelList, path, pathOfTemp]
    * @return: void
    * @Date: 2023/10/31
    */
    private static void writeToExcel(ResultItems resultItems, List<ProductExcel> productExcelList, String path, String pathOfTemp) {
        List<Product> productList;
        // 爬虫有结果时 结果为List
        if(resultItems.get("productList") instanceof List){
            productList = resultItems.get("productList");

            //  只取结果的前三个
            if (productList.size() <= 3) {
                for (Product product : productList) {
                    Date date = new Date();
                    // 创建excel内容的实体类
                    ProductExcel productExcel = new ProductExcel(date, date, product);
                    productExcelList.add(productExcel);
                }
            }else{
                for (Product product : productList.subList(0, 3)) {
                    Date date = new Date();
                    // 创建excel内容的实体类
                    ProductExcel productExcel = new ProductExcel(date, date, product);
                    productExcelList.add(productExcel);
                }
            }
            // 以追加写入的方式，写入excel文件
            FileUtils.excelAppend(productExcelList, path, pathOfTemp);
        }
        // 无结果时 结果为String 此时实例化一个新的ProductExcel
        else if (resultItems.get("productList") instanceof String){
            Float noResultPrice=-1.0f;
            ProductExcel productExcel = new ProductExcel(new Date(), new Date(), resultItems.get("productList"), noResultPrice, "");
            productExcelList.add(productExcel);
            // 以追加写入的方式，写入excel文件
            FileUtils.excelAppend(productExcelList, path, pathOfTemp);
        }
        // 以上情况都不是
        else{
            throw new RuntimeException("爬取的结果为空或者异常");
        }
    }


}
