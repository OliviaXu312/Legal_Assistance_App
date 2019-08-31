package com.example.joan.myapplication;

public class CaseResultData {

    public int listNumber = 3;
    public String[] cname = {"賠償\n金額", "量刑\n", "利息\n金額"};
    public  String[] ct = {"最大值","平均值","最小值"};

    public int percentage;

    public Similar[] similars;
    public Reason[] reasons;
    public Refer[] refers;
    public Compensate compensates;

    public CaseResultData(){
        this.similars = new Similar[listNumber];
        this.reasons = new Reason[listNumber];
        this.refers = new Refer[listNumber];
        this.compensates = new Compensate();
    }

    public CaseResultData(int p, Similar[] similars, Reason[] reasons, Refer[] refers, Compensate compensates){
        this.percentage = p;
        this.similars = similars;
        this.reasons = reasons;
        this.refers = refers;
        this.compensates = compensates;
    }

    public Similar[] getSimilars() {
        return similars;
    }

    public void setSimilars(Similar[] similars) {
        this.similars = similars;
    }

    public Reason[] getReasons() {
        return reasons;
    }

    public void setReasons(Reason[] reasons) {
        this.reasons = reasons;
    }

    public Refer[] getRefers() {
        return refers;
    }

    public void setRefers(Refer[] refers) {
        this.refers = refers;
    }

    public Compensate getCompensates() {
        return compensates;
    }

    public void setCompensates(Compensate compensates) {
        this.compensates = compensates;
    }

    public int getPercentage() { return percentage; }

    public void setPercentage(int percentage) { this.percentage = percentage; }

    public class Similar{
        public String title, subTitle, lawyer;

        public Similar(String title, String subTitle, String lawyer){
            this.title = title;
            this.subTitle = subTitle;
            this.lawyer = lawyer;
        }
    }

    public class Reason{

        public String content;

        public Reason(String content){
            this.content = content;
        }

    }

    public class Refer{

        public String title, subtitle;

        public Refer(String title, String subtitle){

            this.title = title;
            this.subtitle = subtitle;

        }

    }

    public class Compensate{

        public int[][] c = new int[listNumber][];

        public Compensate(){

        }

        public Compensate(int[] c, int[] y, int[] i){

            this.c[0] = c;
            this.c[1] = y;
            this.c[2] = i;

        }

    }

}

