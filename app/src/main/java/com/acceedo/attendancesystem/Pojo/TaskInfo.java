package com.acceedo.attendancesystem.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskInfo implements Parcelable {

    String taskName;
    String taskDesc;
    String taskHours;

    public TaskInfo() {
    }

    protected TaskInfo(Parcel in) {
        taskName = in.readString();
        taskDesc = in.readString();
        taskHours = in.readString();
    }

    public static final Creator <TaskInfo> CREATOR = new Creator <TaskInfo>() {
        @Override
        public TaskInfo createFromParcel(Parcel in) {
            return new TaskInfo(in);
        }

        @Override
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(String taskHours) {
        this.taskHours = taskHours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskName);
        dest.writeString(taskDesc);
        dest.writeString(taskHours);
    }
}
