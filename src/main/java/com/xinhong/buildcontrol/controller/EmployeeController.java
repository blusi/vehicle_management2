package com.xinhong.buildcontrol.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinhong.buildcontrol.pojo.Car;
import com.xinhong.buildcontrol.pojo.DriverInfo;
import com.xinhong.buildcontrol.pojo.Employee;
import com.xinhong.buildcontrol.service.impl.EmployeeServiceImpl;
import com.xinhong.buildcontrol.utils.DatagridResult;
import com.xinhong.buildcontrol.utils.Result;
import com.xinhong.buildcontrol.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "员工管理")
@RestController
@RequestMapping("/employee")
@CrossOrigin
public class EmployeeController {
    @Autowired
    EmployeeServiceImpl employeeService;
    Result result = new Result();
    TimeUtils time = new TimeUtils();


    /**
     * 添加和修改
     * @param
     * @return
     */
    @ApiOperation(value = "员工新增或修改——可用",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "employeeId", value = "员工id", required = false,paramType = "query"),
            @ApiImplicitParam(name = "employeeName", value = "员工姓名", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeeAge", value = "年龄", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeeSex", value = "性别", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeeBirthday", value = "生日", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeeAddress", value = "住址", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeeIdCard", value = "身份证", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeePhone", value = "电话号码", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeeDepartment", value = "部门", required = true,paramType = "query"),
            @ApiImplicitParam(name = "employeeWorkingYears", value = "工龄", required = true,paramType = "query"),

    })
    @RequestMapping(value="/save",method= RequestMethod.GET)//测试所用GET
    public Result save( Employee employee){//@RequestBoy
        try{
            if(employee.getEmployeeId()!= null){
                employee.setEmployeeModified(time.getTime1());
                employee.setEmployeeOperator("初始值");
                employeeService.updateById(employee);
            }else {
                String s = IdUtil.simpleUUID();
                employee.setEmployeeId(s);
                employee.setEmployeeCreate(time.getTime1());
                employee.setEmployeeOperator("初始值");
                employeeService.insert(employee);
            }
            return result.success();
        }catch (Exception e){
            e.printStackTrace();
            return result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "删除——可用",httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "删除，id必须传入", required = true,paramType = "query")
    })
    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id){
        try {
            Employee pojo = new Employee();
            pojo.setEmployeeId(id);
            pojo.setIsDelete(1);
            employeeService.updateById(pojo);
            return result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return result.fail(e.getMessage());
        }
    }

    /**
     * 查询单个
     * @param id
     * @return
     */
    @ApiOperation(value = "用户查询单个——可用",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单个查询，id必须传入", required = true,paramType = "query")
    })
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result get(@PathVariable("id")String id)
    {
        try {
            return result.success("成功",employeeService.selectById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return result.fail(e.getMessage());
        }
    }

    /**
     * 查看所有信息
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public Result list(){
        try {
            EntityWrapper<Employee> entityWrapper = new EntityWrapper<Employee>();
            entityWrapper.and("is_delete=0");
            return result.success("成功",employeeService.selectList(entityWrapper));
        } catch (Exception e) {
            e.printStackTrace();
            return result.fail(e.getMessage());
        }
    }
    /**
     * 查看所有信息
     * @return
     */
    @ApiOperation(value = "分页查询——可用",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "页数", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true,paramType = "query")
    })
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public Result listPage(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize){
        try {
            PageHelper.startPage(currentPage,pageSize);
            List<Employee> carApplies = employeeService.selectList(null);
            PageInfo pageInfo = new PageInfo(carApplies);
            return result.success("成功",new DatagridResult(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
            e.printStackTrace();
            return result.fail(e.getMessage());
        }
    }


}
