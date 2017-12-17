package org.jskele.libs.rpc.impl;

import org.jskele.libs.rpc.RpcMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RpcTransactor {

	@Transactional
	public <RQ, RS> RS execute(RpcMethod<RQ, RS> method, RQ request) {
		return method.execute(request);
	}

}
