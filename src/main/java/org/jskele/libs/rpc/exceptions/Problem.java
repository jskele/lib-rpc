package org.jskele.libs.rpc.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Problem {
	private final ProblemCode code;
	private final String description;
	private final List<FieldError> fieldErrors;

	@Override
	public String toString() {
		return code + ": " + description;
	}
}
