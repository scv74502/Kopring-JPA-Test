package com.example.springbootonkotlin.member.service

import com.example.springbootonkotlin.member.controller.request.MemberCreateRequest
import com.example.springbootonkotlin.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberCreateService(
    val memberRepository: MemberRepository,
) {
    @Transactional
    fun createMember(memberCreateRequest: MemberCreateRequest) {
        val newMember = memberCreateRequest.toEntity()
        memberRepository.saveMember(newMember)
    }
}
