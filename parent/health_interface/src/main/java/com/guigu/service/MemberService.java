package com.guigu.service;

import com.health.pojo.Member;

public interface MemberService {
    public Member findByTelephone(String members);
    public  void add(Member member);
}
