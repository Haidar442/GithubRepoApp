package com.coding.githubrepoapp.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.coding.githubrepoapp.data.remote.dto.Owner


data class GithubRepo(
    val id: Int,
    val name: String?,
    val created_at: String?,
    val stargazers_count: Int,
    val owner: Owner?
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readParcelable(Owner::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(created_at)
        parcel.writeInt(stargazers_count)
        parcel.writeParcelable(owner, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GithubRepo> {
        override fun createFromParcel(parcel: Parcel): GithubRepo {
            return GithubRepo(parcel)
        }

        override fun newArray(size: Int): Array<GithubRepo?> {
            return arrayOfNulls(size)
        }
    }
}