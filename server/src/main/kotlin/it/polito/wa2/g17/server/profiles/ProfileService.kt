package it.polito.wa2.g17.server.profiles

interface ProfileService {

    fun getProfile(email: String): ProfileDTO?

    fun addProfile(profile: ProfileDTO): ProfileDTO?


}