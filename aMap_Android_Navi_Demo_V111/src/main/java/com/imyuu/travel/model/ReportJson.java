package com.imyuu.travel.model;


import com.google.gson.annotations.Expose;

public class ReportJson extends MessageJson implements java.io.Serializable {

    @Expose
    private String reportType;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return super.toString() + "report [ reportType=" + reportType
                + "]";
    }


}


