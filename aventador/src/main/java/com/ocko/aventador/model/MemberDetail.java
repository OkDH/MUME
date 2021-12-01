package com.ocko.aventador.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ocko.aventador.constant.MemberStatus;
import com.ocko.aventador.dao.model.aventador.MemberInfo;

public class MemberDetail implements UserDetails {
	
	private static final long serialVersionUID = -7013410173642495413L;
	
	private MemberInfo memberInfo;
	
	public MemberDetail(MemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}
	
	public MemberInfo getMemberInfo() {
		return this.memberInfo;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(memberInfo.getMemberRoles()));
		return authorities;
	}

	@Override
	public String getUsername() {
		return memberInfo.getMemberEmail();
	}
	
	
	/**
	 * {@link MemberStatus#EXPIRED}<br>
	 * {@link MemberStatus#UNSUBSCRIBED}
	 * @return
	 */
	@Override
	public boolean isAccountNonExpired() {
		if(memberInfo.getMemberStatus().equals(MemberStatus.UNSUBSCRIBED))
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
		if(memberInfo.getMemberStatus().equals(MemberStatus.LOCKED))
			return false;
		if(memberInfo.getMemberStatus().equals(MemberStatus.SLEEP))
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
