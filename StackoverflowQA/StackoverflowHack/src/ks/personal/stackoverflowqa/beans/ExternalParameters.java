package ks.personal.stackoverflowqa.beans;
public class ExternalParameters {

	private ItemsPropertyTags items;
	private String hasMore;
	private String quotaMax;
	private String quotaRemaining;

	public ItemsPropertyTags getItems() {
		return items;
	}

	public void setItems(ItemsPropertyTags items) {
		this.items = items;
	}

	public String getHas_more() {
		return hasMore;
	}

	public void setHas_more(String has_more) {
		this.hasMore = has_more;
	}

	public String getQuota_max() {
		return quotaMax;
	}

	public void setQuota_max(String quota_max) {
		this.quotaMax = quota_max;
	}

	public String getQuota_remaining() {
		return quotaRemaining;
	}

	public void setQuota_remaining(String quota_remaining) {
		this.quotaRemaining = quota_remaining;
	}
	
	public String toString() {
        return "[items = " + items + ", hasMore = " + hasMore + ", quotaMax = " + quotaMax + ", quotaRemaining = " + quotaRemaining +"]";
    }
}
