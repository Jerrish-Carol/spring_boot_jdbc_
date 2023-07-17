package com.isteer.springbootjdbc.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor  
@Getter
@Setter
public class EmployeeResult {
    private Long employeeId;
    private String name;
    private String dob;
    private String gender;
    private boolean isActive;
    private boolean isAccountLocked;
    private String email;
    private String department;
    private Long roleId;
    private List<Address> addresses;
    private List<Role> roles;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor  
    @Getter
    @Setter
    public static class Address {
        private Long addressId;
        private Long employeeId;
        private String street;
        private String city;
        private String state;
        private String country;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor  
    @Getter
    @Setter
    public static class Role {
        private String roles;
        private Long roleId;
        private String project;
        private boolean billable;
        private String hierarchicalLevel;
        private String buName;
        private String buHead;
        private String hrManager;

    }
}
