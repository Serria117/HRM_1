package com.hrm.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data @Accessors(chain = true)
@Builder @NoArgsConstructor @AllArgsConstructor
public class BaseResponse
{
    String code;
    String message;
    Object data;
    List<String> error;
    Boolean succeed = true;
    Integer page;
    Integer size;
    Long totalItems;
    Integer totalPages;

    public static BaseResponse success(Object data)
    {
        return new BaseResponse().setCode("200")
                                 .setMessage("Success")
                                 .setData(data);
    }

    public static BaseResponse success(Page<?> paging)
    {
        return new BaseResponse().setCode("200")
                                 .setPage(paging.getNumber() + 1)
                                 .setSize(paging.getSize())
                                 .setTotalPages(paging.getTotalPages())
                                 .setTotalItems(paging.getTotalElements())
                                 .setMessage("Success")
                                 .setData(paging.toList());
    }

    public static BaseResponse noContent()
    {
        return new BaseResponse().setCode(HttpStatus.NO_CONTENT.toString())
                                 .setMessage("No content found");
    }

    public static BaseResponse error(List<String> errors)
    {
        return new BaseResponse().setError(errors)
                                 .setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                                 .setSucceed(false)
                                 .setMessage("Error");
    }

    public static BaseResponse error(String... errors)
    {
        return new BaseResponse().setError(List.of(errors))
                                 .setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                                 .setSucceed(false)
                                 .setMessage("Error");
    }
}
