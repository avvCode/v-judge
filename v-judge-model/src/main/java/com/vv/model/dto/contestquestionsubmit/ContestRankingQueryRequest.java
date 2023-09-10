package com.vv.model.dto.contestquestionsubmit;


import com.vv.common.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;

/**
 * @author vv
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContestRankingQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赛事id
     */
    private Long contestId;

}
