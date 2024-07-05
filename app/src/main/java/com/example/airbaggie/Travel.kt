import android.os.Parcel
import android.os.Parcelable

data class Travel(
    var datapartenza: String,
    var dataritorno: String,
    var tipoviaggio: String,
    var destination: String,
    var clothing: Array<String>,
    var quantity: MutableList<Int>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArray() ?: arrayOf(),
        parcel.createIntArray()?.toMutableList() ?: mutableListOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(datapartenza)
        parcel.writeString(dataritorno)
        parcel.writeString(tipoviaggio)
        parcel.writeString(destination)
        parcel.writeStringArray(clothing)
        parcel.writeIntArray(quantity.toIntArray())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Travel> {
        override fun createFromParcel(parcel: Parcel): Travel {
            return Travel(parcel)
        }

        override fun newArray(size: Int): Array<Travel?> {
            return arrayOfNulls(size)
        }
    }
}
