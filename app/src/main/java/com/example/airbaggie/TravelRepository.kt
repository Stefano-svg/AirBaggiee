class TravelRepository {
    var travel: ArrayList<Travel> = arrayListOf()
    var cont: Int = -1
    var posizione: Int = 0
    var alertDialogShown = false
    fun updateTravel(index: Int, updatedTravel: Travel) {
        if (index >= 0 && index < travel.size) {
            travel[index] = updatedTravel
        }
    }
}