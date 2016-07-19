package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.api.annotations.MyRequestParam;
import ro.teamnet.zth.appl.domain.Department;
import ro.teamnet.zth.appl.service.DepartmentService;
import ro.teamnet.zth.appl.service.impl.DepartmentServiceImpl;

import java.util.List;

/**
 * Created by user on 14.07.2016.
 */
@MyController(urlPath = "/departments")
public class DepartmentController {

    DepartmentService departmentService = new DepartmentServiceImpl();

    @MyRequestMethod(urlPath = "/all")
    public List<Department> getAllDepartments() {
        return departmentService.findAllDepartments();
    }

    @MyRequestMethod(urlPath = "/one")
    public Department getOneDepartment(@MyRequestParam(name = "id") Long id) {
        return departmentService.findOneDepartment(id);
    }
}
