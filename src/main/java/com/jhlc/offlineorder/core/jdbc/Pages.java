package com.jhlc.offlineorder.core.jdbc;
 
import com.alibaba.fastjson.JSON;
 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@AllArgsConstructor
@NoArgsConstructor
public class Pages {
    /**
     *     第几页
     */
	@Getter @Setter
	private int page;
    /**
     *     每页显示几条内容
     */
	@Getter @Setter
	private int size;
    /**
     *     排序字段
     */
	@Getter @Setter
	private String sortColumn;
    /**
     *     排序方式
     */
	@Getter @Setter
	private String direction;
	
	@Override
	public String toString() {
		return JSON.toJSONString(this, true);
	}
}