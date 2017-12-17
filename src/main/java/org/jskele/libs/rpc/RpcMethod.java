package org.jskele.libs.rpc;

public abstract class RpcMethod<RQ, RS> {

	protected RQ request;

	public RS execute(RQ request) {
		this.request = request;
		return execute();
	}

	public abstract RS execute();

}
