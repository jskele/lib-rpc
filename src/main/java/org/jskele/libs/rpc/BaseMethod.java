package org.jskele.libs.rpc;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseMethod<RQ, RS> extends RpcMethod<RQ, RS> {

	protected RQ request;


	@Override
	public final RS execute(RQ request) {
		this.request = request;

		return execute();
	}
}
