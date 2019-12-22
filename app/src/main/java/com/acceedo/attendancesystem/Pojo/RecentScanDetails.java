package com.acceedo.attendancesystem.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class RecentScanDetails implements Parcelable {

    String empcode,empfirstname,emplastname,supervisor,contractor,department,empintime,empouttime,Userimg;

    public RecentScanDetails(){

    }

    protected RecentScanDetails(Parcel in) {
        empcode = in.readString();
        empfirstname = in.readString();
        emplastname = in.readString();
        supervisor = in.readString();
        contractor = in.readString();
        department = in.readString();
        empintime = in.readString();
        empouttime = in.readString();
        Userimg = in.readString();
    }

    public static final Creator<RecentScanDetails> CREATOR = new Creator<RecentScanDetails>() {
        @Override
        public RecentScanDetails createFromParcel(Parcel in) {
            return new RecentScanDetails(in);
        }

        @Override
        public RecentScanDetails[] newArray(int size) {
            return new RecentScanDetails[size];
        }
    };

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getEmpfirstname() {
        return empfirstname;
    }

    public void setEmpfirstname(String empfirstname) {
        this.empfirstname = empfirstname;
    }

    public String getEmplastname() {
        return emplastname;
    }

    public void setEmplastname(String emplastname) {
        this.emplastname = emplastname;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmpintime() {
        return empintime;
    }

    public void setEmpintime(String empintime) {
        this.empintime = empintime;
    }

    public String getEmpouttime() {
        return empouttime;
    }

    public void setEmpouttime(String empouttime) {
        this.empouttime = empouttime;
    }

    public String getUserimg() {
        return Userimg;
    }

    public void setUserimg(String userimg) {
        Userimg = userimg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(empcode);
        dest.writeString(empfirstname);
        dest.writeString(emplastname);
        dest.writeString(supervisor);
        dest.writeString(contractor);
        dest.writeString(department);
        dest.writeString(empintime);
        dest.writeString(empouttime);
        dest.writeString(Userimg);
    }
}
