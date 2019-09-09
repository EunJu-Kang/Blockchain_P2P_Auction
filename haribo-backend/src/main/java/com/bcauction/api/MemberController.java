package com.bcauction.api;

import com.bcauction.application.IMemberService;
import com.bcauction.domain.Member;
import com.bcauction.domain.exception.DomainException;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MemberController {
    public static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private IMemberService memberService;

    @Autowired
    public MemberController(IMemberService memberService) {
        Assert.notNull(memberService, "memberService 개체가 반드시 필요!");
        this.memberService = memberService;
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public List<Member> 목록조회() {
        List<Member> 목록 = memberService.목록조회();

        if (목록 == null || 목록.isEmpty() )
            throw new EmptyListException("NO DATA");

        return 목록;
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
    public Member 조회(@PathVariable int id) {

        Member member = memberService.조회(id);
        if (member == null) {
            logger.error("NOT FOUND ID: ", id);
            throw new NotFoundException(id + " 회원 정보를 찾을 수 없습니다.");
        }

        return member;
    }

    @RequestMapping(value = "/members/login", method = RequestMethod.POST)
    public Member 로그인(@RequestBody Member member) {
        Member 회원 = memberService.조회(member.get이메일());
        if (!회원.get비밀번호().equals(member.get비밀번호()))
            throw new DomainException("비밀번호가 일치하지 않습니다.");
        return 회원;
    }

    @RequestMapping(value = "/members", method = RequestMethod.POST)
    public Member 추가(@RequestBody Member member) {
        return memberService.추가(member);
    }

    @RequestMapping(value = "/members", method = RequestMethod.PUT)
    public Member 수정(@RequestBody Member member) {
        return memberService.수정(member);
    }

    @RequestMapping(value = "/members/{id}", method = RequestMethod.DELETE)
    public void 삭제(@PathVariable int id) {
        memberService.삭제(id);
    }

}
