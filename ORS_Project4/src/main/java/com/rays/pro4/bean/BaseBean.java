package com.rays.pro4.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * * Parent class of all Beans in application. It contains generic attributes.
 * 
 * @author Ramveer Singh
 *
 */

public abstract class BaseBean implements Serializable, DropdownListBean, Comparable<BaseBean> {

	/** The id. */
	protected long id;
	/** The created by. */
	protected String createdBy;
	/** The modified by. */
	protected String modifiedBy;
	/** The created datetime. */
	protected Timestamp createdDatetime;
	/** The modified datetime. */
	protected Timestamp modifiedDatetime;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/** The created datetime. */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created datetime.
	 *
	 * @param createdDatetime
	 *            the new created datetime
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Sets the modified datetime.
	 *
	 * @param modifiedDatetime
	 *            the new modified datetime
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * Sets the modified datetime.
	 *
	 * @return the modified datetime
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * Gets the created datetime.
	 *
	 * @return the created datetime
	 */
	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}

	/**
	 * Sets the created datetime.
	 *
	 * @param createdDatetime
	 *            the new created datetime
	 */
	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	/**
	 * Gets the modified datetime.
	 *
	 * @return the modified datetime
	 */
	public Timestamp getModifiedDatetime() {
		return modifiedDatetime;
	}

	/**
	 * Sets the modified datetime.
	 *
	 * @param modifiedDatetime
	 *            the new modified datetime
	 */
	public void setModifiedDatetime(Timestamp modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	/**
	 * 
	 *
	 * compareTo
	 */
	public int compareTo(BaseBean next) {
		return getValue().compareTo(next.getValue());
	}

}
