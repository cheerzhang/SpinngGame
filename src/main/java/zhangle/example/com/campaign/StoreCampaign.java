package zhangle.example.com.campaign;

public class StoreCampaign {
    public String campaignid;
    public String campaignname;
    public String filepath;
    public String imgurl;

    public StoreCampaign(){}
    public StoreCampaign(String campaignid,String campaignname,String imgurl) {
        this.campaignid = campaignid;
        this.campaignname = campaignname;
        this.imgurl = imgurl;
    }

    public String getCampaignid() {
        return campaignid;
    }

    public String getCampaignname() {
        return campaignname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setCampaignid(String campaignid) {
        this.campaignid = campaignid;
    }

    public void setCampaignname(String campaignname) {
        this.campaignname = campaignname;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
