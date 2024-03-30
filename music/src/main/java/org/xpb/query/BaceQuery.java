package org.xpb.query;

import lombok.Data;

@Data
public class BaceQuery {
    //第几页
    private Integer pageNum;
    //每页条数
    private Integer pageSize;


    //set get方法对应的一个属性名称：startIndex
    public Integer getStartIndex() {
        return (pageNum-1)*pageSize;
    }
}
