package com.bcauction.domain;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.Serializable;
import java.util.Set;

/**
 * TODO 클래스를 완성한다. 패브릭 네트워크 초기 세팅을 위해 org.hyperledger.fabric.sdk.User를
 * implements한 클래스가 필요하다.
 */
public class FabricUser implements User, Serializable {
	private static final long serialVersionUID = 8077132186383604355L;
	private String name;
	private Set<String> roles;
	private String account;
	private String affiliation;
	private Enrollment enrollment;
	private String msp;

	public FabricUser(String name, String affiliation, String msp, Enrollment enrollment) {
		this.name = name;
		this.affiliation = affiliation;
		this.msp = msp;
		this.enrollment = enrollment;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<String> getRoles() {
		return this.roles;
	}

	@Override
	public String getAccount() {
		return this.account;
	}

	@Override
	public String getAffiliation() {
		return this.affiliation;
	}

	@Override
	public Enrollment getEnrollment() {
		return this.enrollment;
	}

	@Override
	public String getMspId() {
		return this.msp;
	}

	@Override
	public String toString() {
		return "FabricUser [name=" + name + ", roles=" + roles + ", account=" + account + ", affiliation=" + affiliation
				+ ", enrollment=" + enrollment + ", msp=" + msp + "]";
	}

}
