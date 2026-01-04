package com.deer.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class BaseEntity {

    /** 补充字段 */
//    状态
    private Integer status;
//    备注
    private String remark;


    /** 创建与更新信息 */
//    创建者
    private String createById;
//    创建时间
    private Long createTime;

//    更新者
    private String updateById;
//    更新时间
    private Long updateTime;


    /** 分页信息 */
//    当前页数
    @TableField(exist = false)
    private Integer pageNum;
//    查询条数
    @TableField(exist = false)
    private Integer pageSize;

}
