package it.polito.wa2.g17.server.profiles

import it.polito.wa2.g17.server.ticketing.tickets.ProblemType

interface ProfileService {

    fun getProfile(email: String): ProfileDTO
    fun editProfile(email: String, request: EditWorkerDTO): ProfileDTO
    fun createCustomer(request: SignupCustomerDTO): ProfileDTO
    fun createWorker(request: SignupWorkerDTO): ProfileDTO
    fun getWorker(email: String): ProfileDTO
    fun getClient(email: String): ProfileDTO
    fun getWorkers(): List<ProfileDTO>
}