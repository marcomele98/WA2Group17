package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType

interface ProfileService {

    fun getProfile(email: String): ProfileDTO

    fun addProfile(profile: ProfileDTO): ProfileDTO

    fun editProfile(email :String, profile: ProfileDTO): ProfileDTO

    fun getProfilesBySkill(skill: ProblemType): List<ProfileDTO>

    fun createCustomer(request: SignupDTO): ProfileDTO

    fun createExpert(request: SignupDTO): ProfileDTO

    fun createCashier(request: SignupDTO): ProfileDTO
}