package com.lv.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.annotation.Message;

/**
 * @Author: lvrongzhuan
 * @Description:
 * @Date: 2018/8/9 11:41
 * @Version: 1.0
 * modified by:
 */
@Slf4j
@Message
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResult<T> {
    private String code;
    private String msg;
    private T data;

    public static <T> BaseResult<T>  success(T t){
        BaseResult<T> tBaseResult = new BaseResult<>();
        tBaseResult.setCode("000000");
        tBaseResult.setMsg("success");
        tBaseResult.setData(t);
        return tBaseResult;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
