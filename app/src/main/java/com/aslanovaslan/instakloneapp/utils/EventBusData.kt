package com.aslanovaslan.instakloneapp.utils

import com.aslanovaslan.instakloneapp.userModel.UserAccount

class EventBusData {

    internal class SendRegisterData(val sendNumber: String?,val sendEmail: String?,val sendRegisteryCode:String?,val sendRegistryWithEmail:Boolean)

    internal class SendUserProfile(val userAccount: UserAccount)
}