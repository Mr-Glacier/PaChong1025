package Analysis;

import Dao.DaoYiChe;
import Entity.Brand;
import Until.ReadUntil;
import Until.SaveUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Analysis_YiChe {
    static DaoYiChe daoYiChe = new DaoYiChe(0, 0);
    private static SaveUntil saveUntil = new SaveUntil();
    private static ReadUntil readUntil = new ReadUntil();

    //    公用方法:下载文件的方法
    public static void Method_Down(String url, String path, String name) {
//        url 为下载地址,path 为本地路径 name为本地保存文件名称
        try {
            System.out.println("开始下载");
            Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            saveUntil.Method_SaveFile(path + name, document.toString());
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    公用方法:读取文件的方法
    public static String Method_Read(String path, String name) {
        String content = null;
        content = readUntil.Method_ReadFile(path + name);
        return content;
    }
    //    公用方法:下载照片的方法
    public static void Method_DownPic(String urlString, String filename){
        try{
            URL url = new URL(urlString); // 构造URL
            URLConnection con = url.openConnection();  // 打开链接
            con.setConnectTimeout(5*1000);  //设置请求超时为5s
            InputStream is = con.getInputStream();  // 输入流
            byte[] bs = new byte[1024];  // 1K的数据缓冲
            int len;  // 读取到的数据长度
            int i = filename.length();
            for(i--;i>=0 && filename.charAt(i) != '\\' && filename.charAt(i) != '/';i--);
            String s_dir = filename.substring(0, i);
            File dir = new File(s_dir);  // 输出的文件流
            if(!dir.exists()){
                dir.mkdirs();
            }
            OutputStream os = Files.newOutputStream(Paths.get(filename));
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            System.out.println("成功下载一次");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //      1.下载品牌所在页面
    public static void Method_1_BrandDown(String url, String path, String name) {
        Method_Down(url, path, name);
    }

    //      2.解析品牌所在页面
    public static void Method_2_BrandAnalysis(String path, String name) {
        String content = Method_Read(path, name);
        Document document = Jsoup.parse(content);
        Elements Items1 = document.select(".brand-list");
        System.out.println(Items1.size());
        Elements Items2 = Items1.select(".brand-list-item");
        System.out.println(Items2.size());
        for (int i = 0; i < Items2.size(); i++) {
            String letter = Items2.get(i).attr("data-index");
            System.out.println(letter);
            Elements Items3 = Items2.get(i).select(".item-brand");
            for (int j = 0; j < Items3.size(); j++) {
                Element Items4 = Items3.get(j);
                String BrandName = Items4.attr("data-name");
                String BrandUrl = "https://car.yiche.com/" + Items4.select("a").attr("href");
                //System.out.println(BrandUrl);
                String BrandPicUrl = "https:" + Items4.select(".brand-icon.lazyload").attr("data-original");
                //https://car.yiche.com/xuanchegongju/?mid=9
                //System.out.println(BrandPicUrl);
                int BrandId = Integer.parseInt(Items4.attr("data-id"));
                Brand brand = new Brand();
                brand.setC_BrandID(BrandId);
                brand.setC_BrandName(BrandName);
                brand.setC_BrandUrl(BrandUrl);
                brand.setC_BrandPicUrl(BrandPicUrl);
                brand.setC_Letter(letter);
                brand.setC_DownState("否");
                daoYiChe.MethodInsert(brand);
            }
        }
    }

    //      3.下载品牌数据文件
    public static void Method_3_DownBrandPic(String path){
        ArrayList<Object> BrandList = daoYiChe.MethodFind();
        for (int i = 0; i < BrandList.size(); i++) {
            int ID = ((Brand)BrandList.get(i)).getC_BrandID();
            String PicUrl =  ((Brand)BrandList.get(i)).getC_BrandPicUrl();
            System.out.println(ID);
            Method_DownPic(PicUrl,path+ID+".png");
        }
    }

    //      4.下载载车型所在页面
    public static void Method_4_DownBrand(String path){
        ArrayList<Object> BrandList = daoYiChe.MethodFind();
        for (int i = 0; i < BrandList.size(); i++) {
            int ID = ((Brand)BrandList.get(i)).getC_BrandID();
            String BrandUrl =  ((Brand)BrandList.get(i)).getC_BrandUrl();
            String State = ((Brand)BrandList.get(i)).getC_DownState();
            System.out.println(ID);
            if (State.equals("否")){
                Method_Down(BrandUrl,path,ID+".txt");
            }
        }
    }

    public static void main(String[] args) {
        //下载品牌页面
        String BrandURL = "https://car.yiche.com/";
        String BrandFilePath = "F:\\ZKZD\\Java项目\\PaChong1025文件";
        String BrandFileName = "\\BrandFile.txt";
        //      1.下载品牌所在页面
        // Method_1_BrandDown(BrandURL,BrandFilePath,BrandFileName);

        //      2.解析品牌所在页面
        //Method_2_BrandAnalysis(BrandFilePath, BrandFileName);

        //      3.下载图标设置
        String PicPath="F:\\ZKZD\\Java项目\\PaChong1025文件\\品牌图片1027\\";
        //Method_3_DownBrandPic(PicPath);

        //      4.下载车型所在页面
        String BrandPath="F:\\ZKZD\\Java项目\\PaChong1025文件\\车型所在页面\\";
        Method_4_DownBrand(BrandPath);

        //

    }
}
