package edu.utd.security.blueraywebapp.service;

public class InputBean {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InputBean [shiftedIndex=" + shiftedIndex + ", originalString=" + originalString + ", url=" + url
				+ ", priority=" + priority + ", id=" + id + "]";
	}

	// Table Structure.
	public String shiftedIndex = null;
	public String originalString = null;
	public String url=null;
	public int priority=-1;
	public Integer id=null;
	
	public InputBean(String shiftedIndex, String originalString,String url,int priority) {
		super();
		this.shiftedIndex = shiftedIndex;
		this.originalString = originalString;
		this.url=url;
		this.priority=priority;
	}
	
	public InputBean(String shiftedIndex, String originalString,String url,int priority, int id) {
		super();
		this.shiftedIndex = shiftedIndex;
		this.originalString = originalString;
		this.url=url;
		this.priority=priority;
		this.id=id;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the shiftedIndex
	 */
	public String getShiftedIndex() {
		return shiftedIndex;
	}

	/**
	 * @param shiftedIndex
	 *            the shiftedIndex to set
	 */
	public void setShiftedIndex(String shiftedIndex) {
		this.shiftedIndex = shiftedIndex;
	}

	/**
	 * @return the originalString
	 */
	public String getOriginalString() {
		return originalString;
	}
 
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((originalString == null) ? 0 : originalString.hashCode());
		result = prime * result + priority;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputBean other = (InputBean) obj;
		if (originalString == null) {
			if (other.originalString != null)
				return false;
		} else if (!originalString.equals(other.originalString))
			return false;
		if (priority != other.priority)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param originalString
	 *            the originalString to set
	 */
	public void setOriginalString(String originalString) {
		this.originalString = originalString;
	}

}
