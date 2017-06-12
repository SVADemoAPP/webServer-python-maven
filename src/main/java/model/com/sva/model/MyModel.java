package com.sva.model;

public class MyModel
{

    private String myPhone;

    private String otherPhone;

    private String result;

    private String floorNo;
    
    /**
     * @Fields userId 用户id
     */
    private String userId;

    /**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMyPhone()
    {
        return myPhone;
    }

    public void setMyPhone(String myPhone)
    {
        this.myPhone = myPhone;
    }

    public String getOtherPhone()
    {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone)
    {
        this.otherPhone = otherPhone;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getFloorNo()
    {
        return floorNo;
    }

    public void setFloorNo(String floorNo)
    {
        this.floorNo = floorNo;
    }

}
