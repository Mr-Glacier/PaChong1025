package Analysis;

import Dao.DaoCreat;
import Dao.DaoYiChe;
import Entity.Brand;
import Entity.Brand_2;
import Entity.Model;
import Until.ReadUntil;
import Until.SaveUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Analysis_YiChe {

    private static SaveUntil saveUntil = new SaveUntil();
    private static ReadUntil readUntil = new ReadUntil();

    //    公用方法:下载文件的方法
    public  void Method_Down(String url, String path, String name) {
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
    public static void Method_Down(String url, String path, String name,int ID,String tableName ,String columnName) {
//        url 为下载地址,path 为本地路径 name为本地保存文件名称
        try {
            System.out.println("开始下载");
            Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            saveUntil.Method_SaveFile(path + name, document.toString());
            daoYiChe.Method_ChangeState(ID, tableName,columnName);
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
    public void Method_1_BrandDown(String url, String path, String name) {
        Method_Down(url, path, name);
    }

    //      2.解析品牌所在页面
    public  void Method_2_BrandAnalysis(String path, String name,String HeaderUrl) {
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
                String BrandUrl = HeaderUrl+ Items4.select("a").attr("href");
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
    public  void Method_3_DownBrandPic(String path){
        ArrayList<Object> BrandList = daoYiChe.MethodFind();
        for (int i = 0; i < BrandList.size(); i++) {
            int ID = ((Brand)BrandList.get(i)).getC_BrandID();
            String PicUrl =  ((Brand)BrandList.get(i)).getC_BrandPicUrl();
            System.out.println(ID);
            Method_DownPic(PicUrl,path+ID+".png");
        }
    }

    //      4.下载载车型所在页面
    public  void Method_4_DownBrand(String path){
        ArrayList<Object> BrandList = daoYiChe.MethodFind();
        for (Object o : BrandList) {
            int ID = ((Brand) o).getC_ID();
            int BrandID = ((Brand) o).getC_BrandID();
            String BrandUrl = ((Brand) o).getC_BrandUrl();
            String State = ((Brand) o).getC_DownState();
            System.out.println(BrandID);
            if (State.equals("否")) {
                Method_Down(BrandUrl, path, BrandID + ".txt", ID, daoYiChe.getTableName(),"C_DownState");
            }
        }
    }
    //      5.创建表
    public  void Method_5_CreatTableBrandMid(){
        daoCreat1.CreatTable();
        daoCreat2.CreatTable();
    }
    //      6.插入中间表数据
    public  void Method_6_GetBrandEnglish(String path){
        ArrayList<Object> BrandList = daoYiChe.MethodFind();
        for (int i = 0; i < BrandList.size(); i++) {
            int ID = ((Brand)BrandList.get(i)).getC_ID();
            int BrandID = ((Brand)BrandList.get(i)).getC_BrandID();
            String BrandName = ((Brand)BrandList.get(i)).getC_BrandName();

            System.out.println(BrandName);
            String content = readUntil.Method_ReadFile(path+BrandID+".txt");

            Document document = Jsoup.parse(content);
            Elements Items1 = document.select(".search-result-list");
            //System.out.println(Items1.size());
            Elements Items2= Items1.select(".search-result-list-item");
            System.out.println(Items2.size());
            Brand_2 brand_2 = new Brand_2();
            if (Items2.size()==0){
                System.out.println("无数据");
                brand_2.setC_BrandID(BrandID);
                brand_2.setC_BrandName(BrandName);
                brand_2.setC_HavingModel(0);
                brand_2.setC_BrandEnglish("无");
                brand_2.setC_OnSalePageUrl("无");
                daoYiChe2.MethodInsert(brand_2);
            }else {
                Element Items3= Items2.get(0).select("a").get(0);
                String Url  = "https://car.yiche.com"+Items3.attr("href");
                try{
                    Document document1 = Jsoup.parse(new URL(Url).openStream(), "UTF-8", Url);
                    Element ItemsB1 = document1.select(".yiche-breadcrumb_item-link").get(2);
                    String saleUrl = "https://car.yiche.com"+ItemsB1.attr("href");
                    String English = ItemsB1.attr("href").replace("/", "");
                    brand_2.setC_BrandID(BrandID);
                    brand_2.setC_BrandName(BrandName);
                    brand_2.setC_HavingModel(1);
                    brand_2.setC_OnSalePageUrl(saleUrl);
                    brand_2.setC_NotSalePageUrl(saleUrl+"?sale=1");
                    brand_2.setC_BrandEnglish(English);
                    daoYiChe2.MethodInsert(brand_2);
                    Thread.sleep(1000);
                }catch (Exception e){
                    System.out.println(e);
                }

            }
        }
    }


    //      7.下载车型所在页面
    public  void Method_7_DownModelPage(String SalePath,String noSalePath){
        ArrayList<Object> PageList  =  daoYiChe2.MethodFind();
        for (int i = 0; i < PageList.size(); i++) {
            int C_ID = ((Brand_2)PageList.get(i)).getC_ID();
            int Having = ((Brand_2)PageList.get(i)).getC_HavingModel();
            int BrandID = ((Brand_2)PageList.get(i)).getC_BrandID();
            String BrandName = ((Brand_2)PageList.get(i)).getC_BrandName();
            String saleUrl = ((Brand_2)PageList.get(i)).getC_OnSalePageUrl();
            String NoSaleUrl = ((Brand_2)PageList.get(i)).getC_NotSalePageUrl();
            String OnState = ((Brand_2)PageList.get(i)).getC_DownState();
            String StopState = ((Brand_2)PageList.get(i)).getC_DownStateNoSale();
            if (Having==1){
                System.out.println(BrandName);

                if (OnState.equals("否")){
                    Method_Down(saleUrl,SalePath,BrandID+".txt",C_ID,daoYiChe2.TableName,"C_DownState");
                }
                if (StopState.equals("否")){
                    Method_Down(NoSaleUrl,noSalePath,BrandID+".txt",C_ID,daoYiChe2.TableName,"C_DownStateNoSale");
                }
            }
        }
    }

    //      8.
    public  void Method_8_AnalysisBrand(String salepath,String stopPath){
        ArrayList<Object> PageList  =  daoYiChe2.MethodFind();
        for (int i = 0; i < PageList.size(); i++) {
            int C_ID = ((Brand_2)PageList.get(i)).getC_ID();
            int Having = ((Brand_2)PageList.get(i)).getC_HavingModel();
            int BrandID = ((Brand_2)PageList.get(i)).getC_BrandID();
            String BrandName = ((Brand_2)PageList.get(i)).getC_BrandName();
            String url = ((Brand_2)PageList.get(i)).getC_OnSalePageUrl();
            String state = ((Brand_2)PageList.get(i)).getC_DownState();

            if (Having==1){
                String  Salecontent = readUntil.Method_ReadFile(salepath +BrandID+".txt");
                Document Saledocument  = Jsoup.parse(Salecontent);
                Elements Items1 = Saledocument.select(".img-info-layout-vertical.img-info-layout-vertical-center.img-info-layout-vertical-180120");
                if (Items1.size()>0){
                    for (int j = 0; j < Items1.size(); j++) {
                        int ModelID = Integer.parseInt(Items1.get(j).attr("data-id"));
                        String ModelName = Items1.get(j).select(".lazyload").attr("alt");
                        String ModelUrl ="https://car.yiche.com"+ Items1.get(j).select("a").attr("href");
                        String ModelPicUrl = Items1.get(j).select(".lazyload").attr("data-original");

                        Elements Items2 = Items1.get(j).select("span");
                        String tag = "";
                        if (Items2.size()>0){
                             tag = Items2.text();
                            System.out.println(tag);
                        }else {
                            tag = "无";
                        }
                        Model model = new Model();
                        model.setC_ModelID(ModelID);
                        model.setC_ModelName(ModelName);
                        model.setC_ModelUrl(ModelUrl);
                        model.setC_ModelPicUrl(ModelPicUrl);
                        model.setC_BrandID(BrandID);
                        model.setC_BrandName(BrandName);
                        model.setC_DownState("否");
                        model.setC_Tag(tag);
                        model.setC_isSale(1);
                        daoYiChe3.MethodInsert(model);
                    }
                }else {
                    System.out.println(BrandName+"无车型");
                }


                String NOSalContent = readUntil.Method_ReadFile(stopPath+BrandID+".txt");
                Document NoSaledocument  = Jsoup.parse(NOSalContent);
                Elements ItemsB1 = NoSaledocument.select(".img-info-layout-vertical.img-info-layout-vertical-center.img-info-layout-vertical-180120");
                if (ItemsB1.size()>0){
                    for (int j = 0; j < ItemsB1.size(); j++) {
                        int ModelID = Integer.parseInt(ItemsB1.get(j).attr("data-id"));
                        String ModelName = ItemsB1.get(j).select(".lazyload").attr("alt");
                        String ModelUrl ="https://car.yiche.com"+ ItemsB1.get(j).select("a").attr("href");
                        String ModelPicUrl = ItemsB1.get(j).select(".lazyload").attr("data-original");
                        Elements Items2 = ItemsB1.get(j).select("span");
                        String tag = "";
                        if (Items2.size()>0){
                            tag = Items2.text();
                            System.out.println(tag);
                        }else {
                            tag = "无";
                        }
                        Model model = new Model();
                        model.setC_ModelID(ModelID);
                        model.setC_ModelName(ModelName);
                        model.setC_ModelUrl(ModelUrl);
                        model.setC_ModelPicUrl(ModelPicUrl);
                        model.setC_BrandID(BrandID);
                        model.setC_BrandName(BrandName);
                        model.setC_DownState("否");
                        model.setC_Tag(tag);
                        model.setC_isSale(0);
                        daoYiChe3.MethodInsert(model);
                    }
                }

            }
        }
    }

    //9
    public  void Method_9_DownOnSaleModelPic(String salepath,String StopSalePath){
        ArrayList<Object> List = daoYiChe3.MethodFind();
        for (int i = 0; i < List.size(); i++) {
            String PicUrl = ((Model)List.get(i)).getC_ModelPicUrl();
            int ModelID = ((Model)List.get(i)).getC_ModelID();
            int ISSale = ((Model)List.get(i)).getC_isSale();
            if (ISSale==1){
                Method_DownPic(PicUrl,salepath+ModelID+".png");
            }else {
                Method_DownPic(PicUrl,StopSalePath+ModelID+".png");
            }

        }

    }


//    public static void Method_10_AnalysisBrandForNotSaleModel(String path){
//        ArrayList<Object> PageList  =  daoYiChe2.MethodFind();
//        for (int i = 0; i < PageList.size(); i++) {
//            int C_ID = ((Brand_2)PageList.get(i)).getC_ID();
//            int Having = ((Brand_2)PageList.get(i)).getC_HavingModel();
//            int BrandID = ((Brand_2)PageList.get(i)).getC_BrandID();
//            String BrandName = ((Brand_2)PageList.get(i)).getC_BrandName();
//            String url = ((Brand_2)PageList.get(i)).getC_OnSalePageUrl();
//            String state = ((Brand_2)PageList.get(i)).getC_DownState();
//
//            if (Having==1){
//                String  content = readUntil.Method_ReadFile(path+BrandID+".txt");
//                Document document  = Jsoup.parse(content);
//                Elements Items1 = document.select(".img-info-layout-vertical.img-info-layout-vertical-center.img-info-layout-vertical-180120");
////                System.out.println(BrandName);
////                System.out.println(Items1.size());
//                if (Items1.size()>0){
//                    for (int j = 0; j < Items1.size(); j++) {
//                        int ModelID = Integer.parseInt(Items1.get(j).attr("data-id"));
//                        String ModelName = Items1.get(j).select(".lazyload").attr("alt");
//                        String ModelUrl ="https://car.yiche.com"+ Items1.get(j).select("a").attr("href");
//                        String ModelPicUrl = Items1.get(j).select(".lazyload").attr("data-original");
//
//                        Elements Items2 = Items1.get(j).select("span");
//                        String tag = "";
//                        if (Items2.size()>0){
//                            tag = Items2.text();
//                            System.out.println(tag);
//                        }else {
//                            tag = "无";
//                        }
//                        Model model = new Model();
//                        model.setC_ModelID(ModelID);
//                        model.setC_ModelName(ModelName);
//                        model.setC_ModelUrl(ModelUrl);
//                        model.setC_ModelPicUrl(ModelPicUrl);
//                        model.setC_BrandID(BrandID);
//                        model.setC_BrandName(BrandName);
//                        model.setC_DownState("否");
//                        model.setC_Tag(tag);
//                        model.setC_isSale(1);
//                        daoYiChe3.MethodInsert(model);
//                    }
//                }else {
//                    System.out.println(BrandName+"无车型");
//                }
//            }
//        }
//    }

    public static void Method_10_Method(){


    }

    static DaoYiChe daoYiChe = new DaoYiChe(0, 0);
    static DaoCreat daoCreat1 = new DaoCreat(0,1);
    static DaoCreat daoCreat2 = new DaoCreat(0,2);
    static DaoYiChe daoYiChe2 = new DaoYiChe(0, 1);
    static DaoYiChe daoYiChe3 = new DaoYiChe(0, 2);
//    public static void main(String[] args) {
//        //下载品牌页面
//        String BrandURL = "https://car.yiche.com/";
//        String BrandFilePath = "F:\\ZKZD\\Java项目\\PaChong1025文件\\";
//        String BrandFileName = "1_BrandFile.txt";
//        //      1.下载品牌所在页面
//        // Method_1_BrandDown(BrandURL,BrandFilePath,BrandFileName);
//
//        //      2.解析品牌所在页面
//        //Method_2_BrandAnalysis(BrandFilePath, BrandFileName);
//
//        //      3.下载图标设置
//        String PicPath="F:\\ZKZD\\Java项目\\PaChong1025文件\\品牌图片1027\\";
//        //Method_3_DownBrandPic(PicPath);
//
//        //      4.下载车型所在页面
//        String BrandPath="F:\\ZKZD\\Java项目\\PaChong1025文件\\车型页面\\";
//        //Method_4_DownBrand(BrandPath);
//
//        //      5.创建品牌的中间表,获取品牌英文
//        //Method_5_CreatTableBrandMid();
//
//        //        6.获得厂商URL,在售与停售
//        //Method_6_GetBrandEnglish(BrandPath);
//
//        //7.下载厂商URL页面
//        String ModelPath = "F:\\ZKZD\\Java项目\\PaChong1025文件\\2_Model\\";
//        String onSaleModelPath=ModelPath+"OnSale\\";
//        String StopSaleModelPath=ModelPath+"StopSale\\";
//        String onSalePicPath=ModelPath+"OnSalePic\\";
//        String StopSalePicPath=ModelPath+"StopSalePic\\";
//
//        //Method_7_DownModelPage(onSaleModelPath,StopSaleModelPath);
//
//        //
//        //Method_8_AnalysisBrand(onSaleModelPath,StopSaleModelPath);
//
//        // 9 车型图片下载
//        //Method_9_DownOnSaleModelPic(onSalePicPath,StopSalePicPath);
//
//        // 10
//
//
//    }
}
