package Until;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

public class Method_web {


//    public static void main(String[] args){
//        Method_webtest h = new Method_webtest();
//        h.Method_FindYCWB();
//    }

    String webContent = "";

    public String Method_FindYCWB(String Parurl){
        try{
            Playwright pw = Playwright.create();
            Browser b = pw.chromium().launch();
            Page p = b.newPage();
            p.onRequestFinished(this::Method_GetContent);
            p.navigate(Parurl);
            p.waitForLoadState(LoadState.NETWORKIDLE);
            //System.out.println(this.webContent);
            p.close();
            b.close();
            pw.close();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        return webContent;
    }

    //https://mapi.yiche.com/web_api/car_model_api/api/v1/car/config_new_param?cid=508&param=%7B%22cityId%22%3A%22910%22%2C%22carId%22%3A%22159415%22%7D
    private void Method_GetContent(Request re) {
        //car_model_api/api/v1/car/config_new_param
        if (re.url().indexOf("web_api/car_model_api/api/v1/car/config_new_param") > -1){
            Response res = re.response();
            this.webContent = res.text();
        }
    }
}
