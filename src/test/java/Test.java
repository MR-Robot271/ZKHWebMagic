import com.kb.downloader.ZKHDownloader;
import com.kb.pipeline.ExcelPipeline;
import com.kb.pojo.Keyword;
import com.kb.processor.ZKHProcessor;
import com.kb.utils.FileUtils;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String baseUrl="https://www.zkh.com/search.html?keywords=";
//        String baseUrl="https://www.zkh.com/search.html?keywords=";
        String searchName="绿联（UGREEN）六类RJ45水晶头镀金 50248 100个装";

        // 获取网页地址
        String keywordPath="D:\\feishuDownloads\\test5.xlsx";
        List<Keyword> keywords = FileUtils.getKeywords(keywordPath);
        List<String> urls = new ArrayList<>();
        for (Keyword keyword:keywords){
            String noBlankModelParameters=keyword.getModelParameters().replaceAll(" ", "");
            keyword.setModelParameters(noBlankModelParameters);
            String noBlankType=keyword.getType().replaceAll(" ", "");
            keyword.setType(noBlankType);
            String url=baseUrl+keyword;
            urls.add(url);
        }
        // 需要把List转为String数组 addUrl只能用String数组才能添加多个
        String[] strings = urls.toArray(new String[0]);


        ZKHDownloader zkhDownloader = new ZKHDownloader();
        zkhDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("123.96.128.112", 21446)));
        Spider spider = Spider.create(new ZKHProcessor())
                .addUrl(strings)
//                .addUrl("https://www.zkh.com/search.html?keywords=绿联(UGREEN)六类RJ45水晶头镀金 50248 100个装")
//                .addPipeline(new ConsolePipeline())
                .setDownloader(zkhDownloader)
                .addPipeline(new ExcelPipeline())
                // 多线程可能会触发反爬虫
                .thread(1);
        spider.run();
        // 关闭ChromeDriver的浏览器
        zkhDownloader.closeWebDriver();
    }
}
