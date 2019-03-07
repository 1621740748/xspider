package fund.jrj.com.xspider.bo;

import java.util.Arrays;

public class PageResult {
	private int ok;
	private int type;
	private String content;
	private byte[] image;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	

	public int getOk() {
		return ok;
	}
	public void setOk(int ok) {
		this.ok = ok;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + Arrays.hashCode(image);
		result = prime * result + ok;
		result = prime * result + type;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageResult other = (PageResult) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (!Arrays.equals(image, other.image))
			return false;
		if (ok != other.ok)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
