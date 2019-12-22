package com.acceedo.attendancesystem.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentAttendance implements Parcelable
{
    String timeadde;
    boolean isselectable;

    public StudentAttendance(){

    }

    protected StudentAttendance(Parcel in) {
        timeadde = in.readString();
        isselectable = in.readByte() != 0;
    }

    public static final Creator <StudentAttendance> CREATOR = new Creator <StudentAttendance>() {
        @Override
        public StudentAttendance createFromParcel(Parcel in) {
            return new StudentAttendance(in);
        }

        @Override
        public StudentAttendance[] newArray(int size) {
            return new StudentAttendance[size];
        }
    };

    public String getTimeadde() {
        return timeadde;
    }

    public void setTimeadde(String timeadde) {
        this.timeadde = timeadde;
    }

    public boolean isIsselectable() {
        return isselectable;
    }

    public void setIsselectable(boolean isselectable) {
        this.isselectable = isselectable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timeadde);
        dest.writeByte((byte) (isselectable ? 1 : 0));
    }
}
