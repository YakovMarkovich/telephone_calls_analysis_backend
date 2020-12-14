package io.shelfy.telephone_analysis_application.dto;

public class CallersPageableDto {
    public long itemsTotal;
    public int itemsOnPage;
    public int currentPage;
    public CallerDto[] callers;
    public boolean moreItems;

    public CallersPageableDto() {
    }

    public CallersPageableDto(long itemsTotal, int itemsOnPage, int currentPage, CallerDto[] callers, boolean moreItems) {
        this.itemsTotal = itemsTotal;
        this.itemsOnPage = itemsOnPage;
        this.currentPage = currentPage;
        this.callers = callers;
        this.moreItems = moreItems;
    }


}
	
	
	
	
