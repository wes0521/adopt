package com.wes.adopt.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value="Pet对象", description="")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "宠物姓名")
    @TableField("petName")
    private String petName;

    @ApiModelProperty(value = "宠物种类")
    @TableField("petType")
    private String petType;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty(value = "图片")
    private String pic;

    @ApiModelProperty(value = "0 没有申请领养 1 被申请领养 ")
    private Integer state;

    @ApiModelProperty(value = "领养的主人 0:没有,大于0:有")
    private Integer uid;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "逻辑删除")
    private Integer is_deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;
//    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
