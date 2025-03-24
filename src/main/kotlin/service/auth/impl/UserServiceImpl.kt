package io.github.llh4github.ksas.service.auth.impl

import io.github.llh4github.ksas.dbmodel.auth.User
import io.github.llh4github.ksas.service.BaseServiceImpl
import io.github.llh4github.ksas.service.auth.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService, BaseServiceImpl<User>(User::class) {

}
