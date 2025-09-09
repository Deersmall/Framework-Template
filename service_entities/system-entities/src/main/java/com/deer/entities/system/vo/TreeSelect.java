package com.deer.entities.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Treeselect树结构实体类
 *
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeSelect implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 节点ID */
    @TableField(exist = false)
    private String id;
    /** 节点名称 */
    @TableField(exist = false)
    private String label;
    @TableField(exist = false)
    private String path;
    /** 子节点 */
    @TableField(exist = false)
    private List<TreeSelect> children;

}
