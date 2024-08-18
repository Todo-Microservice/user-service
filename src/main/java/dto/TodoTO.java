package dto;

public class TodoTO {
	private Integer id;
	private String title;
	private String desc;
	private Integer createdBy;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Override
	public String toString() {
		return "TodoTO [id=" + id + ", title=" + title + ", desc=" + desc + ", createdBy=" + createdBy + "]";
	}
}
