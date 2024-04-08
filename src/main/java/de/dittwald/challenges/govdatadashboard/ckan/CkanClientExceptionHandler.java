package de.dittwald.challenges.govdatadashboard.ckan;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.TimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;

/**
 * Handles the GovDataCkanClient exceptions
 */
@ControllerAdvice
public class CkanClientExceptionHandler {

	@ExceptionHandler(ConnectTimeoutException.class)
	public String govdataConnectTimeoutException(ConnectTimeoutException e) {
		return "timeout";
	}

	@ExceptionHandler(ReadTimeoutException.class)
	public String govdataReadTimeoutException(ReadTimeoutException e) {
		return "timeout";
	}

	@ExceptionHandler(WriteTimeoutException.class)
	public String govdataWriteTimeoutException(WriteTimeoutException e) {
		return "timeout";
	}

	@ExceptionHandler(TimeoutException.class)
	public String govdataConnectTimeoutException(TimeoutException e) {
		return "timeout";
	}

}
