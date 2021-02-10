package com.zambo.zambo_mterminal100.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActiveServiceResponse {


    @SerializedName("status")
    @Expose
    public List<ActiveService> status;

    public List<ActiveService> getStatus() {
        return status;
    }

    public void setData(List<ActiveService> status) {
        this.status = status;
    }
    public ActiveServiceResponse (){

    }

    @NotNull
    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+"]";
    }

    public class ActiveService {

        @SerializedName("mainId")
        @Expose
        private String mainId;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("mainStatus")
        @Expose
        private String mainStatus;

        @SerializedName("serviceId")
        @Expose
        private String serviceId;

        @SerializedName("subServicetitle")
        @Expose
        private String subServicetitle;

        @SerializedName("subServicename")
        @Expose
        private String subServicename;

        @SerializedName("subServicestatus")
        @Expose
        private String subServicestatus;

        @SerializedName("subServiceId")
        @Expose
        private String subServiceId;

        public ActiveService (){

        }

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMainStatus() {
            return mainStatus;
        }

        public void setMainStatus(String mainStatus) {
            this.mainStatus = mainStatus;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getSubServicetitle() {
            return subServicetitle;
        }

        public void setSubServicetitle(String subServicetitle) {
            this.subServicetitle = subServicetitle;
        }

        public String getSubServicename() {
            return subServicename;
        }

        public void setSubServicename(String subServicename) {
            this.subServicename = subServicename;
        }

        public String getSubServicestatus() {
            return subServicestatus;
        }

        public void setSubServicestatus(String subServicestatus) {
            this.subServicestatus = subServicestatus;
        }

        public String getSubServiceId() {
            return subServiceId;
        }

        public void setSubServiceId(String subServiceId) {
            this.subServiceId = subServiceId;
        }
    }
}
