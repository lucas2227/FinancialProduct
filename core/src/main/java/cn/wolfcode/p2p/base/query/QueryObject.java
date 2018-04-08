package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2017/12/14.
 */
@Setter@Getter
public class QueryObject {
    private Integer currentPage = 1;
    private Integer pageSize = 5;
}
