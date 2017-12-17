package org.jskele.libs.rpc.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jskele.libs.rpc.exceptions.Problem;
import org.jskele.libs.rpc.exceptions.Problems;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("api/rpc")
class RpcController {

	private final ObjectMapper objectMapper;
	private final RpcExecutor rpcExecutor;

	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping("{methodName}")
	void handleRpcRequest(
			@PathVariable String methodName,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {

		MDC.put("requestId", UUID.randomUUID().toString());

		byte[] requestBytes = ByteStreams.toByteArray(httpRequest.getInputStream());

		log.info("input {}", new String(requestBytes, StandardCharsets.UTF_8));

		RpcResult result = rpcExecutor.execute(methodName, requestBytes);

		httpResponse.setStatus(result.getStatus().value());
		httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
		httpResponse.getOutputStream().write(result.getBytes());

		log.info("output {}", new String(result.getBytes(), StandardCharsets.UTF_8));
	}

	@ExceptionHandler
	void handleException(Exception e, HttpServletResponse httpResponse) throws IOException {
		httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

		Problem problem = Problems.internalError(e);
		log.error("Exception while processing request", e);
		objectMapper.writeValue(httpResponse.getOutputStream(), problem);
	}

}
