package com.ocko.aventador.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ocko.aventador.constant.MemberStatus;
import com.ocko.aventador.dao.model.aventador.MemberAccount;

public class MemberDetail implements  UserDetails {
	
	private static final long serialVersionUID = -7013410173642495413L;
	
	private MemberAccount memberAccount;
	
	public MemberDetail(MemberAccount memberAccount) {
		this.memberAccount = memberAccount;
	}
	
	public MemberAccount getMemberAccount() {
		return this.memberAccount;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(memberAccount.getMemberRoles()));
		return authorities;
	}

	@Override
	public String getUsername() {
		return memberAccount.getMemberEmail();
	}
	
	
	/**
	 * {@link MemberStatus#EXPIRED}<br>
	 * {@link MemberStatus#UNSUBSCRIBED}
	 * @return
	 */
	@Override
	public boolean isAccountNonExpired() {
		if(memberAccount.getMemberStatus().equals(MemberStatus.UNSUBSCRIBED))
			return false;
		return true;
	}
	
	/**
	 * {@link MemberStatus#LOCKED}<br>
	 * {@link MemberStatus#SLEEP}
	 * @return
	 */
	@Override
	public boolean isAccountNonLocked() {
		if(memberAccount.getMemberStatus().equals(MemberStatus.LOCKED))
			return false;
		if(memberAccount.getMemberStatus().equals(MemberStatus.SLEEP))
			return false;
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	/**
	 * @return
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
}
