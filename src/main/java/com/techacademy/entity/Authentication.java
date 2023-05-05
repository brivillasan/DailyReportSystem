package com.techacademy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "authentication")
public class Authentication {

    //グループを作成
    public static interface Create {}
    public static interface Update {}

    /** 役割の列挙型 */
    public static enum Role {
        管理者, 一般
    }

    /** 社員番号 */
    @Id
    @NotEmpty
    @Column(length = 20, nullable = false)
    private String code;

    /** パスワード */
    @NotEmpty
    @Column(length = 255, nullable = false)
    private String password;

    /** 権限 */
    @NotEmpty
    @Enumerated(EnumType.STRING)
    private Role role;

    /** ユーザID */
    @OneToOne
    @JoinColumn(name="employee_id", referencedColumnName="id")
    private Employee employee;
}
