package com.techacademy.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techacademy.entity.Authentication;
import com.techacademy.entity.Employee;
import com.techacademy.service.AuthenticationService;
import com.techacademy.service.EmployeeService;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }
    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("employeelist", service.getEmployeeList());
        model.addAttribute("size", service.getEmployeeList().size());
        // country/list.htmlに画面遷移
        return "employee/list";
    }
    /** 登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute Employee employee) {
        // User登録画面に遷移
        return "employee/register";
    }

    // ----- 詳細画面 -----
    @GetMapping(value = { "/detail", "/detail/{id}" })
    public String getEmployee(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のクラスを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        // Modelに登録
        model.addAttribute("employee", employee);
        // country/detail.htmlに画面遷移
        return "employee/detail";
    }
    /** 更新画面を表示 */
    @GetMapping("/update/{id}/")
    public String getEmployee(@PathVariable("id") Integer id, Employee employee, Model model) {
        if (id != null) {
            // Modelに登録
            model.addAttribute("employee", service.getEmployee(id));
            // User更新画面に遷移
        } else {
            model.addAttribute("employee", employee);
        }
        return "employee/update";
    }

    /** User更新処理 */
    @PostMapping("/update/{id}/")
    public String postEmployee(@PathVariable("id") Integer id,@Validated Employee employee , BindingResult res ,Model model) {
        //@RequestParam(name="newpass") Set<String> newpass
        if(res.hasErrors()) {
            // エラーあり

            return getEmployee(null,employee,model);
         }
        //元々のエラーは引数が足りなかった。画面上のemployee(社員番号・氏名・パスワード・権限)をそのままemployee(氏名・削除フラグ・createdAt・updateAt)に保存できない。
        //ので、個別のデータを取得し、上書きしていく。
        Employee motoemployee= service.getEmployee(id);//employee(氏名・deleteフラグ・createdAt・updateAt)のデータをmotoemployeeに格納している。
        motoemployee.setName(employee.getName());
        if (employee.getAuthentication().getPassword() != "" ) {
        //すでに取得しているentity.employeeにauthenticationも含まれている。
        motoemployee.getAuthentication().setPassword(employee.getAuthentication().getPassword());
        }
        motoemployee.getAuthentication().setRole(employee.getAuthentication().getRole());


        //パスワードは空なら上書きしない。


        // User登録
        service.saveUser(motoemployee);
        // 一覧画面にリダイレクト
        return "redirect:/employee/list";
    }
    /** User削除処理 */
    @GetMapping("/delete/{id}/")
    public String deleteRun(@PathVariable("id") Integer id, Model model) {
        // Userを削除
        service.deleteUser(id);
        // 一覧画面にリダイレクト
         return "redirect:/employee/list";

    }
}