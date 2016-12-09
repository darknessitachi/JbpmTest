package org.darkness.jbpmtest;

/**
 * @author Darkness
 * 
 * QQ: 893951837 Email: darkness_sky@qq.com
 * Blog:http://depravedAngel.javaeye.com/
 * 
 * Copyright (c) 2009 by Darkness
 * 
 * @date Apr 18, 2009 9:17:36 AM
 * @version 1.0
 */
public class Document {

	private int id;
	private String title;
	private String content;
	private String creator;
	private Long processIntanceId;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the processIntanceId
	 */
	public Long getProcessIntanceId() {
		return processIntanceId;
	}

	/**
	 * @param processIntanceId
	 *            the processIntanceId to set
	 */
	public void setProcessIntanceId(Long processIntanceId) {
		this.processIntanceId = processIntanceId;
	}
}
