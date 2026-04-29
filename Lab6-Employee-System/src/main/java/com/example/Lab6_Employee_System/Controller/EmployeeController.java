package com.example.Lab6_Employee_System.Controller;

import com.example.Lab6_Employee_System.Api.ApiResponse;
import com.example.Lab6_Employee_System.Modle.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<ArrayList<Employee>> getEmployee() {
        return ResponseEntity.status(200).body(employees);

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully "));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable String id, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getID().equals(id)) {
                employees.set(i, employee);
                return ResponseEntity.status(200).body(new ApiResponse("Employee updated successfully "));

            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee with id " + id + " not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getID().equals(id)) {
                employees.remove(i);
                return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully "));

            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee with id " + id + " not found"));
    }

    @GetMapping("/get/by-position/{position}")
    public ResponseEntity<?> getEmployeeByPosition(@PathVariable String position) {
        ArrayList<Employee> employeeByPosition = new ArrayList<>();
        if (position.equalsIgnoreCase("supervisor") || position.equalsIgnoreCase("coordinator")) {
            for (Employee e : employees) {
                if (e.getPosition().equalsIgnoreCase(position)) {
                    employeeByPosition.add(e);
                }
            }
        }else     {
            return ResponseEntity.status(400).body("position parameter is valid either 'supervisor' or 'coordinator' only");
        }

        if (employeeByPosition.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No Employee found in this position " + position));
        }
        return ResponseEntity.status(200).body(employeeByPosition);

    }

    @GetMapping("get/by-Age-range/{minAge}/{maxAge}")
    public ResponseEntity<?> getEmployeeByAgeRange(@PathVariable int minAge, @PathVariable int maxAge) {
        ArrayList<Employee> employeeByAgeRange = new ArrayList<>();
        if (minAge < 25 && maxAge < 25) {
            return ResponseEntity.status(400).body(new ApiResponse("Invalid Age minAge and maxAge must be greater than 25"));
        }
        for (Employee e : employees) {
            if (e.getAge() >= minAge && e.getAge() <= maxAge) {
                employeeByAgeRange.add(e);
            }
        }
        if (employeeByAgeRange.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No employees found in the age range " + minAge + " - " + maxAge));
        }
        return ResponseEntity.status(200).body(employeeByAgeRange);
    }

    @PatchMapping("/apply-For-Annual-Leave/{id}")
    public ResponseEntity<ApiResponse> applyForAnnualLeave(@PathVariable String id) {
        for (Employee e : employees) {
            if (e.getID().equals(id)) {
                if (e.isOnLeave()) {
                    return ResponseEntity.status(400).body(new ApiResponse("The employee already on leave"));
                }
                if (e.getAnnualLeave() > 0) {
                    e.setOnLeave(true);
                    e.setAnnualLeave(e.getAnnualLeave() - 1);
                    return ResponseEntity.status(200).body(new ApiResponse("Annual leave applied for successfully"));
                } else {
                    return ResponseEntity.status(400).body(new ApiResponse("Employee has no annual leave balance"));
                }
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee with  id " + id + " not found"));
    }

    @GetMapping("/get/employee-with-no-annual-leave")
    public ResponseEntity<?> getEmployeeWithNoAnnualLeave() {
        ArrayList<Employee> employeeWithNoAnnualLeave = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getAnnualLeave() == 0) {
                employeeWithNoAnnualLeave.add(e);
            }
        }
        if (employeeWithNoAnnualLeave.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No employees found who used up all their annual leave"));
        }
        return ResponseEntity.status(200).body(employeeWithNoAnnualLeave);

    }
   @PatchMapping(   "/promote-employee/{supervisorId}/{employeeId}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String supervisorId, @PathVariable String employeeId) {
        Employee supervisor = null;
        Employee targetEmployee = null;
        for (Employee e : employees) {
            if (e.getID().equals(supervisorId)) supervisor = e;
            if (e.getID().equals(employeeId)) targetEmployee = e;
        }
        if (supervisor == null) return ResponseEntity.status(404).body(new ApiResponse("Supervisor not found"));
        if (targetEmployee == null) return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));

        if (!supervisor.getPosition().equalsIgnoreCase("supervisor")) {
            return ResponseEntity.status(403).body(new ApiResponse("Only supervisors can make this request"));
        }

        if (targetEmployee.getAge() < 30) {
            return ResponseEntity.status(400).body(new ApiResponse("The Employee age must be at least 30 years"));
        }

        if (targetEmployee.isOnLeave()) {
            return ResponseEntity.status(400).body(new ApiResponse("Confirm That employee is not currently on leave"));
        }
        targetEmployee.setPosition("supervisor");
        return ResponseEntity.status(200).body(new ApiResponse("Employee " + employeeId + " promoted successfully"));
    }
}
