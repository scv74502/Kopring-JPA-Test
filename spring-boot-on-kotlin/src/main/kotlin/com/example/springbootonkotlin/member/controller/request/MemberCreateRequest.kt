package com.example.springbootonkotlin.member.controller.request

import com.example.springbootonkotlin.member.entity.MemberEntity
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class MemberCreateRequest(
    @field:NotBlank
    @field:Size(min = 2, max = 50, message = "회원 이름은 2-50자 사이여야 합니다")
    @field:Pattern(
        regexp = "^[가-힣a-zA-Z]*$",
        message = "회원 이름은 공백 없이 한글, 영문으로만 구성되어야 합니다",
    )
    val memberName: String,
) {
    fun toEntity(): MemberEntity = MemberEntity(name = this.memberName)
}
