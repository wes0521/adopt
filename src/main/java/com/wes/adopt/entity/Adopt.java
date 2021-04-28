package com.wes.adopt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wes
 * @since 2020-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_adopt")
@ApiModel(value="Adopt对象", description="")
public class Adopt implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户表id的外键")
    private Integer userId;

    @ApiModelProperty(value = "宠物表id的外键")
    private Integer petId;

    @ApiModelProperty(value = "收养时间")
    @TableField("adoptTime")
    private Date adoptTime;

    @ApiModelProperty(value = "是否同意被领养 0 是不同意 1 还在审核 2 是同意")
    private Integer state;


}
