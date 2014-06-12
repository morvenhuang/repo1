package mh.bamgr.models;

import java.sql.Date;

public class ExpenseIncomeBean {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Date getEiDate() {
		return eiDate;
	}
	public void setEiDate(Date eiDate) {
		this.eiDate = eiDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private int categoryId;
	private String categoryName;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	private int amount;
	private Date eiDate;
	private String description;	
	private int isIncome;
	public int getIsIncome() {
		return isIncome;
	}
	public void setIsIncome(int isIncome) {
		this.isIncome = isIncome;
	}
}
