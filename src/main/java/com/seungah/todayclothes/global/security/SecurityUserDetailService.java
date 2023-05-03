package com.seungah.todayclothes.global.security;

import static com.seungah.todayclothes.global.type.UserStatus.ACTIVE;
import static com.seungah.todayclothes.global.type.UserStatus.INACTIVE;

import com.seungah.todayclothes.global.exception.CustomException;
import com.seungah.todayclothes.global.exception.ErrorCode;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.global.type.UserStatus;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityUserDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Member member = memberRepository.findById(Long.parseLong(userId))
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

		return new User(String.valueOf(member.getId()), member.getPassword(),
			getAuthorities(member.getUserStatus()));

	}

	private Collection<? extends GrantedAuthority> getAuthorities(UserStatus status) {
		if (status == UserStatus.ACTIVE) {
			return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + ACTIVE.name()));
		} else if (status == UserStatus.INACTIVE) {
			return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + INACTIVE.name()));
		} else {
			return Collections.emptyList();
		}
	}
}
