package com.vv.oj.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vv.oj.model.entity.UserContest;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @author vv
 */
@Data
public class UserContestVO {
    /**
     * id
     */
    private Long id;

    /**
     * 创建用户id
     */
    private UserVO userVO;

    /**
     * 赛事VO
     */
    private ContestVO contestVO;

    /**
     * AC数
     */
    private Integer acceptNum;

    /**
     * 总提交数
     */
    private Integer total;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 总耗时
     */
    private String totalTime;

    /**
     * 比赛状态 0-正在参加（不显示） 1-结束（显示）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;
    
    private static final long serialVersionUID = 1L;
    private final static Gson GSON = new Gson();
    /**
     * 包装类转对象
     *
     * @param userContestVO
     * @return
     */
    public static UserContest voToObj(UserContestVO userContestVO) {
        if (userContestVO == null) {
            return null;
        }
        UserContest userContest = new UserContest();
        BeanUtils.copyProperties(userContestVO, userContest);
        return userContest;
    }

    /**
     * 对象转包装类
     *
     * @param userContest
     * @return
     */
    public static UserContestVO objToVo(UserContest userContest) {
        if (userContest == null) {
            return null;
        }
        UserContestVO userContestVO = new UserContestVO();
        BeanUtils.copyProperties(userContest, userContestVO);
        return userContestVO;
    }
}
