package com.example.springbootonkotlin.member.repository

import com.example.springbootonkotlin.member.entity.MemberEntity
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {
    override fun findMemberByName(member: MemberEntity): List<MemberEntity> {
        TODO()
    }

    override fun saveMember(member: MemberEntity) {
        memberJpaRepository.save(member)
    }
}
