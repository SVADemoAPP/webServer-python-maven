package com.sva.model;

public class RegisterModel
{    
    /**
     * @Fields userId 用户id，可以使ip、mac或手机号等
     */
    private String userId;

    private String userName;

    private String passWord;

    private String phoneNumber;

    private long times;

    private int status;

    private int role;

    private long otherPhone;

    private int isTrue;
    
    private String loginStatus;

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

	public String getLoginStatus()
    {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus)
    {
        this.loginStatus = loginStatus;
    }

    public long getOtherPhone()
    {
        return otherPhone;
    }

    public void setOtherPhone(long otherPhone)
    {
        this.otherPhone = otherPhone;
    }

    public int getIsTrue()
    {
        return isTrue;
    }

    public void setIsTrue(int isTrue)
    {
        this.isTrue = isTrue;
    }

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role = role;
    }

    public String getPassWord()
    {
        return passWord;
    }

    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public long getTimes()
    {
        return times;
    }

    public void setTimes(long times)
    {
        this.times = times;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

}
