package me.zhengjie.base;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Zheng Jie
 * @date 2019年10月24日20:48:53
 */
@Getter
@Setter
public class BaseDTO  implements Serializable {

    private String createBy;

    private String updateBy;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "";
    }
}
