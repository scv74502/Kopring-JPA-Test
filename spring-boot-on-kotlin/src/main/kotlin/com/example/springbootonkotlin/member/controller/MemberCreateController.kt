package com.example.springbootonkotlin.member.controller

import com.example.springbootonkotlin.member.controller.request.MemberCreateRequest
import com.example.springbootonkotlin.member.service.MemberCreateService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/member")
class MemberCreateController(
    val memberCreateService: MemberCreateService,
) {
    fun createMember(
        @RequestBody memberCreateRequest: MemberCreateRequest,
    ) {
        memberCreateService.createMember(memberCreateRequest)
    }
}
