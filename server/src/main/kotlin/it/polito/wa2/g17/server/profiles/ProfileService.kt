package it.polito.wa2.g17.server.profiles

interface ProfileService {

    fun getProfile(email: String): ProfileDTO
    fun editWorker(email: String, request: EditWorkerDTO): ProfileDTO
    fun createCustomer(request: SignupCustomerDTO): ProfileDTO
    fun createWorker(request: SignupWorkerDTO): ProfileDTO
    fun getWorker(email: String): ProfileDTO
    fun getClient(email: String): ProfileDTO
    fun getWorkers(): List<ProfileDTO>
    fun deleteWorker(email: String)
    fun resetPassword(email: String, newPassword: String, oldPassword: String, confirmPassword: String)
}