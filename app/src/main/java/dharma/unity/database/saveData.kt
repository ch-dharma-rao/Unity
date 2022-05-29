package dharma.unity.database

class NeedHelpData(
    val name: String,
    val email: String,
    val isVerifiedEmail: Boolean,
    val phoneNo: String,
    val latitude: String,
    val longitude: String,
    val msg: String
)


class BeVolunteerData(
    val name: String,
    val email: String,
    val isVerifiedEmail: Boolean,
    val phoneNo: String,
    val latitude: String,
    val longitude: String,
    val msg: String
)


class AlertsData(
    val name: String,
    val phoneNo: String,
    val latitude: String,
    val longitude: String,
    val msg: String
) {
    constructor() : this("", "", "", "", "") {

    }


}

class ContactsData(val name: String, val phoneNo: String) {
    constructor() : this("", "") {

    }
}

class FindMissingData(
    val name: String,
    val phoneNo: String,
    val address: String,
    val last_seen_locatin: String,
    val photoURI: String
) {
    constructor() : this("", "", "", "", "") {

    }
}