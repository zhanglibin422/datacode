package com.huntkey.rx.sceo.form.client.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.huntkey.rx.commons.utils.rest.Result;

public class SceoGlobleException extends BasicErrorController {

	public SceoGlobleException(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
	}

	@Override
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        try {
        	response.setCharacterEncoding("UTF-8");
			response.getWriter().write(createErrorMsg());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = new HashMap<String,Object>();
		body.put("error", createErrorMsg());
		HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}
	private String createErrorMsg(){
		Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("请检查URL和参数是否正确");
        return result.toString();
	}
}
