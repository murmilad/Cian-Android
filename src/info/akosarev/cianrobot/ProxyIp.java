package info.akosarev.cianrobot;

public class ProxyIp {
	private String ip;
	private Integer port;
	
	public ProxyIp(String ip, Integer port) {
		this.ip = ip;
		this.port= port;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
}
