package zhangle.example.com.campaign;

public class Store {
    public String storeid;
    public String storename;
    public int campaignno;

    public Store(){
    }

    public Store(String storeid, String storename,int campaignno ){
        this.storeid = storeid;
        this.storename = storename;
        this.campaignno = campaignno;
    }

    public String getStoreid(){
        return this.storeid;
    }

    public void setStoreid(String storeid){
        this.storeid = storeid;
    }

    public String getStorename(){
        return this.storename;
    }

    public void setStorename(String storename){
        this.storename = storename;
    }

    public int getCampaignno(){ return this.campaignno; }

    public void setCampaignno(int campaignno){ this.campaignno = campaignno; }
}
