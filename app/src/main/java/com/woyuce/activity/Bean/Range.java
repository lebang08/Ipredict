package com.woyuce.activity.Bean;

public class Range {

	private String id;
	private String title;

	public Range() {
		super();
	}

	public Range(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Range [id=" + id + ", title=" + title + "]";
	}

}
