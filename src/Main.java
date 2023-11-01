import Analysis.Analysis_YiChe;

public class Main {
    public static void main(String[] args) {

        String BrandURL = "https://car.yiche.com/";
        String HeaderUrl = "https://car.yiche.com";
        String FilePath = "F:\\ZKZD\\Java项目\\PaChong1025文件\\";
        String BrandFileName = "1_BrandFile.txt";
        //进行设置
        Analysis_YiChe AS =  new Analysis_YiChe();

        //      1.下载品牌列表文件
        AS.Method_1_BrandDown(BrandURL,FilePath,BrandFileName);

        //      2.解析品牌列表文件
        AS.Method_2_BrandAnalysis(FilePath,BrandFileName,HeaderUrl);

        String PicPath=FilePath+"品牌图片1027\\";
        //      3.下载品牌图标
        AS.Method_3_DownBrandPic(PicPath);

        String FirstModelPath=FilePath+"车型页面\\";
        //      4.下载车型页面首页第一页,为得到车型整体页面
        AS.Method_4_DownBrand(FirstModelPath);



    }
}