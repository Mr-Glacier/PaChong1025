package Entity;

public class Model {
    private int C_ID;
    private int C_BrandID;
    private String C_BrandName;
    private int C_ModelID;
    private String C_ModelName;
    private String C_ModelUrl;
    private String C_ModelPicUrl;
    private String C_DownState;
    private String C_Tag;
    private int C_isSale;

    public int getC_ID() {
        return C_ID;
    }

    public String getC_BrandName() {
        return C_BrandName;
    }

    public void setC_BrandName(String c_BrandName) {
        C_BrandName = c_BrandName;
    }

    public void setC_ID(int c_ID) {
        C_ID = c_ID;
    }

    public int getC_BrandID() {
        return C_BrandID;
    }

    public void setC_BrandID(int c_BrandID) {
        C_BrandID = c_BrandID;
    }

    public int getC_ModelID() {
        return C_ModelID;
    }

    public void setC_ModelID(int c_ModelID) {
        C_ModelID = c_ModelID;
    }

    public String getC_ModelName() {
        return C_ModelName;
    }

    public void setC_ModelName(String c_ModelName) {
        C_ModelName = c_ModelName;
    }

    public String getC_ModelUrl() {
        return C_ModelUrl;
    }

    public void setC_ModelUrl(String c_ModelUrl) {
        C_ModelUrl = c_ModelUrl;
    }

    public String getC_ModelPicUrl() {
        return C_ModelPicUrl;
    }

    public void setC_ModelPicUrl(String c_ModelPicUrl) {
        C_ModelPicUrl = c_ModelPicUrl;
    }

    public String getC_DownState() {
        return C_DownState;
    }

    public void setC_DownState(String c_DownState) {
        C_DownState = c_DownState;
    }

    public String getC_Tag() {
        return C_Tag;
    }

    public void setC_Tag(String c_Tag) {
        C_Tag = c_Tag;
    }

    public int getC_isSale() {
        return C_isSale;
    }

    public void setC_isSale(int c_isSale) {
        this.C_isSale = c_isSale;
    }
}
