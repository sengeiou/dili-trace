package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信APP
 *
 * @author travis.cheng
 */
@Table(name = "`applets_config`")
public class WxApp extends BaseDomain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
    private Long id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_secret")
    private String appSecret;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "access_token_expires_in")
    private Integer accessTokenExpiresIn;

    @Column(name = "access_token_update_time")
    private Date accessTokenUpdateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getAccessTokenExpiresIn() {
		return accessTokenExpiresIn;
	}

	public void setAccessTokenExpiresIn(Integer accessTokenExpiresIn) {
		this.accessTokenExpiresIn = accessTokenExpiresIn;
	}

	public Date getAccessTokenUpdateTime() {
		return accessTokenUpdateTime;
	}

	public void setAccessTokenUpdateTime(Date accessTokenUpdateTime) {
		this.accessTokenUpdateTime = accessTokenUpdateTime;
	}
}
