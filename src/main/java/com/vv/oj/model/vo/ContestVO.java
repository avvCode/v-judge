package com.vv.oj.model.vo;

import com.vv.oj.model.entity.Contest;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author vv
 */
@Data
public class ContestVO {
    /**
     * id
     */
    private Long id;

    /**
     * 赛事名称
     */
    private String title;

    /**
     * 赛事描述
     */
    private String description;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建用户
     */
    private UserVO userVO;

    /**
     * 赛事状态 0-锁定 1-开启 2-正在进行 3-结束
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 包装类转对象
     *
     * @param contestVO
     * @return
     */
    public static Contest voToObj(ContestVO contestVO) {
        if (contestVO == null) {
            return null;
        }
        Contest contest = new Contest();
        BeanUtils.copyProperties(contestVO, contest);
        return contest;
    }

    /**
     * 对象转包装类
     *
     * @param contest
     * @return
     */
    public static ContestVO objToVo(Contest contest) {
        if (contest == null) {
            return null;
        }
        ContestVO contestVO = new ContestVO();
        BeanUtils.copyProperties(contest, contestVO);
        return contestVO;
    }

    private static final long serialVersionUID = 1L;
}
