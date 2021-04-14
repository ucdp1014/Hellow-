package com.guigu.service.impl;

import com.guigu.dao.MemberDao;
import com.guigu.service.MemberService;
import com.health.pojo.Member;
import com.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("memberService")
    public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String members) {
        return memberDao.findByTelephone(members);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            password=MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }
}
