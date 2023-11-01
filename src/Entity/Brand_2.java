package Entity;

public class Brand_2 {
    private int C_ID;
    private String C_BrandName;
    private int C_BrandID;
    private int C_HavingModel;//是否拥有车型,有1无0
    private String C_BrandEnglish;
    private String C_OnSalePageUrl;
    private String C_NotSalePageUrl;

    public String getC_NotSalePageUrl() {
        return C_NotSalePageUrl;
    }

    public void setC_NotSalePageUrl(String c_NotSalePageUrl) {
        C_NotSalePageUrl = c_NotSalePageUrl;
    }

    private String C_DownState;

    private String C_DownStateNoSale;

    public String getC_DownStateNoSale() {
        return C_DownStateNoSale;
    }

    public void setC_DownStateNoSale(String c_DownStateNoSale) {
        C_DownStateNoSale = c_DownStateNoSale;
    }

    public String getC_DownState() {
        return C_DownState;
    }

    public void setC_DownState(String c_DownState) {
        C_DownState = c_DownState;
    }

    public String getC_OnSalePageUrl() {
        return C_OnSalePageUrl;
    }

    public void setC_OnSalePageUrl(String c_OnSalePageUrl) {
        C_OnSalePageUrl = c_OnSalePageUrl;
    }

    public int getC_ID() {
        return C_ID;
    }

    public void setC_ID(int c_ID) {
        C_ID = c_ID;
    }

    public String getC_BrandName() {
        return C_BrandName;
    }

    public void setC_BrandName(String c_BrandName) {
        C_BrandName = c_BrandName;
    }

    public int getC_BrandID() {
        return C_BrandID;
    }

    public void setC_BrandID(int c_BrandID) {
        C_BrandID = c_BrandID;
    }

    public int getC_HavingModel() {
        return C_HavingModel;
    }

    public void setC_HavingModel(int c_HavingModel) {
        C_HavingModel = c_HavingModel;
    }

    public String getC_BrandEnglish() {
        return C_BrandEnglish;
    }

    public void setC_BrandEnglish(String c_BrandEnglish) {
        C_BrandEnglish = c_BrandEnglish;
    }
}
