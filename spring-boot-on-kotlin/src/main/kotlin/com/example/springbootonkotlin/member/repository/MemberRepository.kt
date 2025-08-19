package com.example.springbootonkotlin.member.repository

import com.example.springbootonkotlin.member.entity.MemberEntity

interface MemberRepository {
    fun findMemberByName(member: MemberEntity): List<MemberEntity>

    fun saveMember(member: MemberEntity)
}
