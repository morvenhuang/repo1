package mh.bamgr.common;

//an object used to return the result of server operation when doing ajax call
public class AjaxResult {
	private String status;
	private String message;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
