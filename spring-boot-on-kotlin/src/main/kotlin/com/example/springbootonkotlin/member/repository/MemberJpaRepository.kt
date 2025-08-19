package com.example.springbootonkotlin.member.repository

import com.example.springbootonkotlin.member.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberEntity, Long>
