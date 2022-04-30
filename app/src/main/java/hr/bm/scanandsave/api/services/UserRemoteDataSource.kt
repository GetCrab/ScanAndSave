package hr.bm.scanandsave.api.services

import hr.bm.scanandsave.base.BaseDataSource
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService
): BaseDataSource() {
    fun getUser() = userService.getUser()
}