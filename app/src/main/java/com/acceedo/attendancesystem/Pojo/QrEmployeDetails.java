package com.acceedo.attendancesystem.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class QrEmployeDetails implements Parcelable {
    String emp_id, emp_code, emp_first_name, emp_last_name, emp_address, emp_city, emp_state, emp_phone1, emp_phone2, emp_email, emp_salary, emp_profile_pic, emp_role_id, emp_supervisor, emp_contractor,
            emp_dept_id, emp_date_of_join, emp_multi_skill, emp_active_status, created_by, supervisor,
            contractor, department, userpic, intime, outtime;

    public QrEmployeDetails() {

    }

    protected QrEmployeDetails(Parcel in) {
        emp_id = in.readString();
        emp_code = in.readString();
        emp_first_name = in.readString();
        emp_last_name = in.readString();
        emp_address = in.readString();
        emp_city = in.readString();
        emp_state = in.readString();
        emp_phone1 = in.readString();
        emp_phone2 = in.readString();
        emp_email = in.readString();
        emp_salary = in.readString();
        emp_profile_pic = in.readString();
        emp_role_id = in.readString();
        emp_supervisor = in.readString();
        emp_contractor = in.readString();
        emp_dept_id = in.readString();
        emp_date_of_join = in.readString();
        emp_multi_skill = in.readString();
        emp_active_status = in.readString();
        created_by = in.readString();
        supervisor = in.readString();
        contractor = in.readString();
        department = in.readString();
        userpic = in.readString();
        intime = in.readString();
        outtime = in.readString();
    }

    public static final Creator<QrEmployeDetails> CREATOR = new Creator<QrEmployeDetails>() {
        @Override
        public QrEmployeDetails createFromParcel(Parcel in) {
            return new QrEmployeDetails(in);
        }

        @Override
        public QrEmployeDetails[] newArray(int size) {
            return new QrEmployeDetails[size];
        }
    };

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getEmp_first_name() {
        return emp_first_name;
    }

    public void setEmp_first_name(String emp_first_name) {
        this.emp_first_name = emp_first_name;
    }

    public String getEmp_last_name() {
        return emp_last_name;
    }

    public void setEmp_last_name(String emp_last_name) {
        this.emp_last_name = emp_last_name;
    }

    public String getEmp_address() {
        return emp_address;
    }

    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    public String getEmp_city() {
        return emp_city;
    }

    public void setEmp_city(String emp_city) {
        this.emp_city = emp_city;
    }

    public String getEmp_state() {
        return emp_state;
    }

    public void setEmp_state(String emp_state) {
        this.emp_state = emp_state;
    }

    public String getEmp_phone1() {
        return emp_phone1;
    }

    public void setEmp_phone1(String emp_phone1) {
        this.emp_phone1 = emp_phone1;
    }

    public String getEmp_phone2() {
        return emp_phone2;
    }

    public void setEmp_phone2(String emp_phone2) {
        this.emp_phone2 = emp_phone2;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_salary() {
        return emp_salary;
    }

    public void setEmp_salary(String emp_salary) {
        this.emp_salary = emp_salary;
    }

    public String getEmp_profile_pic() {
        return emp_profile_pic;
    }

    public void setEmp_profile_pic(String emp_profile_pic) {
        this.emp_profile_pic = emp_profile_pic;
    }

    public String getEmp_role_id() {
        return emp_role_id;
    }

    public void setEmp_role_id(String emp_role_id) {
        this.emp_role_id = emp_role_id;
    }

    public String getEmp_supervisor() {
        return emp_supervisor;
    }

    public void setEmp_supervisor(String emp_supervisor) {
        this.emp_supervisor = emp_supervisor;
    }

    public String getEmp_contractor() {
        return emp_contractor;
    }

    public void setEmp_contractor(String emp_contractor) {
        this.emp_contractor = emp_contractor;
    }

    public String getEmp_dept_id() {
        return emp_dept_id;
    }

    public void setEmp_dept_id(String emp_dept_id) {
        this.emp_dept_id = emp_dept_id;
    }

    public String getEmp_date_of_join() {
        return emp_date_of_join;
    }

    public void setEmp_date_of_join(String emp_date_of_join) {
        this.emp_date_of_join = emp_date_of_join;
    }

    public String getEmp_multi_skill() {
        return emp_multi_skill;
    }

    public void setEmp_multi_skill(String emp_multi_skill) {
        this.emp_multi_skill = emp_multi_skill;
    }

    public String getEmp_active_status() {
        return emp_active_status;
    }

    public void setEmp_active_status(String emp_active_status) {
        this.emp_active_status = emp_active_status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
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

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emp_id);
        dest.writeString(emp_code);
        dest.writeString(emp_first_name);
        dest.writeString(emp_last_name);
        dest.writeString(emp_address);
        dest.writeString(emp_city);
        dest.writeString(emp_state);
        dest.writeString(emp_phone1);
        dest.writeString(emp_phone2);
        dest.writeString(emp_email);
        dest.writeString(emp_salary);
        dest.writeString(emp_profile_pic);
        dest.writeString(emp_role_id);
        dest.writeString(emp_supervisor);
        dest.writeString(emp_contractor);
        dest.writeString(emp_dept_id);
        dest.writeString(emp_date_of_join);
        dest.writeString(emp_multi_skill);
        dest.writeString(emp_active_status);
        dest.writeString(created_by);
        dest.writeString(supervisor);
        dest.writeString(contractor);
        dest.writeString(department);
        dest.writeString(userpic);
        dest.writeString(intime);
        dest.writeString(outtime);
    }
}
